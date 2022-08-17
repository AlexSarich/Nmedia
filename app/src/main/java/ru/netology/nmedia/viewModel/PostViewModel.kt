package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.dto.EditPostResult
import ru.netology.nmedia.dto.Post

import ru.netology.nmedia.utils.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository = FilePostRepository(application)

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreen = SingleLiveEvent<EditPostResult?>()
    val navigateToVideoWatching = SingleLiveEvent<String?>()
    val navigateToPostCardFragmentEvent = SingleLiveEvent<Int>()

    val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String, videosUrl: String?) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy (
            content = content,
            videosUrl = videosUrl
                ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "today",
            likes = 0u,
            shares = 0u,
            views = 0u,
            videosUrl = videosUrl
        )
        repository.save(post)
        currentPost.value = null
    }

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
        repository.share(post.id)
    }
    override fun onDeleteClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
        navigateToPostContentScreen.value = EditPostResult(post.content, post.videosUrl)
    }
    fun onAddButtonClicked() {
        navigateToPostContentScreen.call()
    }
    override fun onVideoClicked(post: Post) {
        navigateToVideoWatching.value = post.videosUrl
    }
    override fun onPostClicked(post: Post) {
        currentPost.value = post
        navigateToPostCardFragmentEvent.value = post.id
    }
}