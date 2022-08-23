package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.PostCardFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel


class PostCardFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val args by navArgs<PostCardFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PostCardFragmentBinding.inflate(layoutInflater)

        val postId = args.postId
        val viewHolder = PostAdapter.viewHolder(binding.postCardFragmentLayout, viewModel)

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            viewHolder.bind(post)

            binding.postCardFragmentLayout.postOptionButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                viewModel.onDeleteClicked(post)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.edit -> {
                                viewModel.onEditClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
        }

        viewModel.navigateToVideoWatching.observe(viewLifecycleOwner) { videoUrl ->
            val intent = Intent()
                .apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(videoUrl)
                }
            val videoWatchingIntent =
                Intent.createChooser(intent, getString(R.string.watch_video))
            startActivity(videoWatchingIntent)
        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }
            val shareIntent =
                Intent.createChooser(intent, getString(R.string.share_chooser))
            startActivity(shareIntent)
        }

        viewModel.navigateToPostContentScreen.observe(viewLifecycleOwner) { editPostResult ->
            val direction =
                PostCardFragmentDirections.postCardFragmentToPostContentFragment(
                    editPostResult?.newContent,
                    editPostResult?.newVideoUrl
                )
            findNavController().navigate(direction)
        }
        return binding.root
    }
}