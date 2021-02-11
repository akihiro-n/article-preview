package com.example.articlepreview.infra

import com.example.articlepreview.infra.model.TagDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    fun getPopularTags(perPage: Int = DEFAULT_PER_PAGE, page: Int): Flow<List<TagDto>> = flow {
        qiitaApi.getTags(perPage, page, SORT_QUERY_COUNT)
    }
}