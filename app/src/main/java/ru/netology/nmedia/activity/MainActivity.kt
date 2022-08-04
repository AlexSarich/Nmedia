package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddButtonClicked()

        }

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(
                intent, getString(R.string.share_chooser)
            )
            startActivity(shareIntent)
        }
        val postActivityResultLauncher =
            registerForActivityResult(PostActivity.ResultContract) { editPostResult ->
            editPostResult?.newContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClicked(editPostResult.newContent, editPostResult.newVideosUrl)
        }
        viewModel.navigateToPostContentScreen.observe(this) {
            postActivityResultLauncher.launch(it)
        }

        viewModel.navigateToVideoWatching.observe(this) { videoUrl ->
            val intent = Intent()
                .apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(videoUrl)
                }
            val videoWatchingIntent =
                Intent.createChooser(intent, getString(R.string.watch_video))
            startActivity(videoWatchingIntent)
        }
    }
}


