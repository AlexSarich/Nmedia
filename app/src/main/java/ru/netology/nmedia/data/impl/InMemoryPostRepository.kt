package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository: PostRepository {

    private var nextId = GENERATED_POST_AMOUNT

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should noy be null"
        }

    override val data = MutableLiveData(
            listOf(
                Post(
                    id = 1000,
                    author = "Me",
                    content = "Hello world!",
                    published = "20.12.2012",
                    likes = 0u,
                    shares = 0u,
                    views = 0u,
                    videosUrl = "https://www.youtube.com/watch?v=jA_N18TJyK0"
                )
            )
                + List(GENERATED_POST_AMOUNT) { index ->
            Post(
                id = (index + 1),
                author = "Me",
                content = "$index Hello world!",
                published = "20.12.2012",
                likes = 0u,
                shares = 0u,
                views = 0u
            )
        }
    )

    override fun like(postId: Int) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1u else it.likes + 1u
            )
        }
    }

    override fun share(postId: Int) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(shares = it.shares + 1u)
        }
    }

    override fun delete(postId: Int) {
        data.value = posts.filter { it.id != postId }
    }

    // save
    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        data.value = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        data.value = listOf(post.copy(id = nextId++)) + posts
    }
    //

    private companion object {
        const val GENERATED_POST_AMOUNT = 100
    }
}