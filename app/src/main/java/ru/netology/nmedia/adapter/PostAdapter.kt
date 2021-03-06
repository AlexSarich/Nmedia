package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import kotlin.math.roundToInt

internal class PostAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostAdapter.viewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(inflater, parent, false)
        return viewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class viewHolder(
        private val binding: PostListItemBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.postOptionButton).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onDeleteClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.postLikeButton.setOnClickListener { listener.onLikeClicked(post) }
            binding.postShareButton.setOnClickListener { listener.onShareClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                postUserName.text = post.author
                postDate.text = post.published
                postText.text = post.content
                postLikeButton.setImageResource(getLikeIconResId(post.likedByMe))
                postNumberLikes.text = getStringFromInt(post.likes)
                postNumberShares.text = getStringFromInt(post.shares)
                postNumberView.text = getStringFromInt(post.views)
                postOptionButton.setOnClickListener { popupMenu.show() }
            }
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

    private class NoNumberException(message: String) : RuntimeException()

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}