package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.postLayout.render(post)
        }

        binding.postLayout.postLikeButton.setOnClickListener {
            viewModel.onLikeClicked()
        }
        binding.postLayout.postShareButton.setOnClickListener {
            viewModel.onShareClicked()
        }
    }

    private fun PostListItemBinding.render(post: Post) {
        postUserName.text = post.author
        postDate.text = post.published
        postText.text = post.content
        postLikeButton.setImageResource(getLikeIconResId(post.likedByMe))
        postNumberLikes.text = getStringFromInt(post.likes)
        postNumberShares.text = getStringFromInt(post.shares)
        postNumberView.text = getStringFromInt(post.views)
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_button_24 else R.drawable.ic_like_button_24dp

    private fun getStringFromInt(num: UInt): String {
        val numDouble = num.toDouble()
        return when (num) {
            in 0u..999u -> num.toString()
            in 1000u..9999u -> "${(numDouble / 1000).roundToInt()}K"
            in 10_000u..999_999u -> "${(numDouble / 100).roundToInt() /10}K"
            in 1_000_000u..Int.MAX_VALUE.toUInt() -> "${(numDouble / 1_000_000).roundToInt()}M"
            else -> throw NoNumberException("No number")
        }
    }
}

class NoNumberException(message: String) : RuntimeException()

