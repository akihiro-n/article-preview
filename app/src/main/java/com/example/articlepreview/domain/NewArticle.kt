package com.example.articlepreview.domain

data class NewArticle(
    val title: String,
    val planeBody: String,
    val markdownBody: String,
    val tags: List<String>,
    val hasSourceCodeBlock: Boolean,
    val userImage: String,
    val images: List<String>
)