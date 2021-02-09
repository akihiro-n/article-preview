package com.example.articlepreview.infra.model

data class Article(
    val title: String,
    val tags: List<ArticleTag>,
    val body: String
)