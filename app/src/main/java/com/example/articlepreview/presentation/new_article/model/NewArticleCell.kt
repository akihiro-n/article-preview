package com.example.articlepreview.presentation.new_article.model

import androidx.annotation.StringRes
import com.example.articlepreview.R
import com.example.articlepreview.data.model.ArticleDto
import com.example.articlepreview.data.model.TagDto
import retrofit2.HttpException

/**
 * 新着記事一覧画面のRecyclerViewのセルに表示するデータを定義
 */
sealed class NewArticleCell {

    object Empty : NewArticleCell()

    object Loading : NewArticleCell()

    data class Tags(val tags: List<TagDto>) : NewArticleCell()

    data class NewArticle(
        /**
         * 記事のデータ
         */
        val value: ArticleDto,

        /**
         * 記事内にソースコードがあるかどうか
         */
        val hasSourceCodeBlock: Boolean
    ) : NewArticleCell()

    data class Error(val cause: Throwable) : NewArticleCell() {
        @StringRes
        val messageResId = when (cause) {
            is HttpException -> R.string.error_rate_limit_get_new_article
            else -> R.string.error_unknown
        }
    }
}