package com.example.articlepreview.data

import com.example.articlepreview.data.model.ArticleDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val qiitaApi: QiitaApi
) {

    companion object {
        private const val DEFAULT_PER_PAGE = 20
    }

    fun getArticles(perPage: Int = DEFAULT_PER_PAGE, page: Int = 1) = flow {
        val articles = qiitaApi.getArticles("$perPage", "$page")
        emit(articles)
    }.flowOn(Dispatchers.IO)
}