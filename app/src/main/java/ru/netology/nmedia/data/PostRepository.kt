package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    fun like(postId: Int)
    fun share(postId: Int)
    fun delete(postId: Int)
    fun save(post: Post)

    companion object {
        const val NEW_POST_ID = 0
    }
}