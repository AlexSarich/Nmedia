package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: UInt,
    val shares: UInt,
    val views: UInt,
    val videosUrl: String? = null
        )