package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository: PostRepository {

    private val posts get() = checkNotNull(data.value) {
        "Data value should noy be null"
    }

    override val data = MutableLiveData(
        List(10) { index ->
            Post(
                id = (index + 1).toUInt(),
                author = "Me",
                content = "$index Hello world!",
                published = "20.12.2012",
                likes = 0u,
                shares = 0u,
                views = 0u
            )
        }
    )

    override fun like(postId: UInt) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1u else it.likes + 1u
            )
        }
    }

    override fun share(postId: UInt) {
        data.value = posts.map {
            if (it.id != postId) it
            else it.copy(shares = it.shares + 1u)
        }
    }
}