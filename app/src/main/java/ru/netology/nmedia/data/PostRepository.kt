package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    fun like(postId: UInt)
    fun share(postId: UInt)
    fun delete(postId: UInt)
    fun save(post: Post)
    fun cancelEdit()

    companion object {
        const val NEW_POST_ID = 0u
    }
}