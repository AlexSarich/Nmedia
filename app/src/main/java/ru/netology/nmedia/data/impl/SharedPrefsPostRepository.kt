package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post
import kotlin.properties.Delegates

class SharedPrefsPostRepository(application: Application): PostRepository {

    private val prefs = application.getSharedPreferences("repo", Context.MODE_PRIVATE)

    private var nextId = GENERATED_POST_AMOUNT

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should noy be null"
        }
        set(value) {
            prefs.edit {
                val serializedPosts = Json.encodeToString(value)
                putString(POST_PREFS_KEY, serializedPosts)
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val serializedPosts = prefs.getString(POST_PREFS_KEY, null)
        val posts: List<Post> = if (serializedPosts != null) {
            Json.decodeFromString(serializedPosts)
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun like(postId: Int) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1u else it.likes + 1u
            )
        }
    }

    override fun share(postId: Int) {
        posts = posts.map {
            if (it.id != postId) it
            else it.copy(shares = it.shares + 1u)
        }
    }

    override fun delete(postId: Int) {
        posts = posts.filter { it.id != postId }
    }

    // save
    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = nextId++)) + posts
    }
    //

    private companion object {
        const val GENERATED_POST_AMOUNT = 100
        const val POST_PREFS_KEY = "posts"
    }
}