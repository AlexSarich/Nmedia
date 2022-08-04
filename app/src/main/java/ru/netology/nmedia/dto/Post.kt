package ru.netology.nmedia.dto

data class Post (
    val id: UInt,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    val likes: UInt,
    val shares: UInt,
    val views: UInt,
    val videosUrl: String? = null
        )