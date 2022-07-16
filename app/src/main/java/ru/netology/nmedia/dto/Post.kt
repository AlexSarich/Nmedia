package ru.netology.nmedia.dto

data class Post (
    val id: UInt,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: UInt,
    var shares: UInt,
    var views: UInt
        )