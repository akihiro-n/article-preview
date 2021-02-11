package com.example.articlepreview.infra

import com.example.articlepreview.infra.model.ArticleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val qiitaApi: QiitaApi
) {

    companion object {
        private const val DEFAULT_PER_PAGE = 20
    }

    fun getArticles(perPage: Int = DEFAULT_PER_PAGE, page: Int): Flow<List<ArticleDto>> = flow {
        qiitaApi.getArticles(perPage, page)
    }
}