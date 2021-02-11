package com.example.articlepreview.data.model

data class ArticleDto(
    val title: String,
    val tags: List<ArticleTagDto>,
    val body: String
)