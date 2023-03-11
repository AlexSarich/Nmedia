package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.Group
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
            fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
                referencedIds.forEach { id ->
                    rootView.findViewById<View>(id).setOnClickListener(listener)
                }
            }
            binding.postLikeButton.setOnClickListener { listener.onLikeClicked(post) }
            binding.postShareButton.setOnClickListener { listener.onShareClicked(post) }
            binding.postPlayButton.setOnClickListener { listener.onVideoClicked(post) }
            binding.postVideoGroup.setAllOnClickListener { listener.onVideoClicked(post) }
            binding.postOptionButton.setOnClickListener { popupMenu.show() }
            binding.groupPost.setAllOnClickListener { listener.onPostClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                postUserName.text = post.author
                postDate.text = post.published
                postText.text = post.content
                postLikeButton.isChecked = post.likedByMe
                postLikeButton.text = getStringFromInt(post.likes)
                //postShareButton.text = getStringFromInt(post.shares)
                //postNumberView.text = getStringFromInt(post.views)
                postVideoGroup.visibility =
                    if (post.videosUrl.isNullOrBlank()) View.GONE else View.VISIBLE
            }
        }

        private fun getStringFromInt(num: Int): String {
            val numDouble = num.toDouble()
            return when (num) {
                in 0..999 -> num.toString()
                in 1000..9999 -> "${(numDouble / 1000).roundToInt()}K"
                in 10_000..999_999 -> "${(numDouble / 100).roundToInt() /10}K"
                in 1_000_000..Int.MAX_VALUE -> "${(numDouble / 1_000_000).roundToInt()}M"
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