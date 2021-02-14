package com.example.articlepreview.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TagRepository @Inject constructor(
    private val qiitaApi: QiitaApi
) {

    companion object {

        private const val DEFAULT_PER_PAGE = 20

        /**
         * タグ一覧を記事数順でソートするクエリ
         */
        private const val SORT_QUERY_COUNT = "count"
    }

    fun getPopularTags(perPage: Int = DEFAULT_PER_PAGE, page: Int = 1) = flow {
        val tags = qiitaApi.getTags("$perPage", "$page", SORT_QUERY_COUNT)
        emit(tags)
    }.flowOn(Dispatchers.IO)
}