package com.example.articlepreview.data

import com.example.articlepreview.data.model.ArticleDto
import com.example.articlepreview.data.model.TagDto
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