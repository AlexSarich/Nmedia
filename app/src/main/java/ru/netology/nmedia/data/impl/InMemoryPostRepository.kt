package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository: PostRepository {
    override val data = MutableLiveData(
        Post(
        id = 0u,
        author = "Me",
        content = "Hello world!",
        published = "20.12.2012",
        likes = 100u,
        shares = 2u,
        views = 3_000_700u
    ))

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value should noy be null"
        }
        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
            likes = if (currentPost.likedByMe) currentPost.likes - 1u else currentPost.likes + 1u
        )
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value should noy be null"
        }
        val sharedPost = currentPost.copy(
            shares = currentPost.shares + 1u
        )
        data.value = sharedPost
    }
}