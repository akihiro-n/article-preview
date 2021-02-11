package com.example.articlepreview.infra.model

data class ArticleDto(
    val title: String,
    val tags: List<ArticleTagDto>,
    val body: String
)