package ru.netology.nmedia.data.impl

import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel
import ru.netology.nmedia.dto.Post

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {


    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) dao.insert(post.toEntity())
        else dao.updateContentById(post.id, post.content)
    }

    override fun like(postId: Long) {
        dao.likeById(postId)
    }

//    override fun share(postId: Int) {
//        TODO("Not yet implemented")
//    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
    }



}