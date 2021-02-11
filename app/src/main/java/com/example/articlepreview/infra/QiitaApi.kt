package com.example.articlepreview.infra

import com.example.articlepreview.infra.model.ArticleDto
import com.example.articlepreview.infra.model.TagDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QiitaApi {

    @GET("tags")
    suspend fun getTags(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ): List<TagDto>

    @GET("items")
    suspend fun getArticles(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): List<ArticleDto>
}