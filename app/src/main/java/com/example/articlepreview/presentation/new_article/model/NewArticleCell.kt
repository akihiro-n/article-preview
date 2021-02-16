package com.example.articlepreview.presentation.new_article.model

import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import com.example.articlepreview.R
import com.example.articlepreview.data.model.ArticleDto
import com.example.articlepreview.data.model.TagDto
import com.example.articlepreview.util.ComparableListItem
import retrofit2.HttpException

/**
 * 新着記事一覧画面のRecyclerViewのセルに表示するデータを定義
 */
sealed class NewArticleCell : ComparableListItem {

    object Empty : NewArticleCell()

    object Loading : NewArticleCell()

    sealed class SectionTitle(open val titleResId: Int) : NewArticleCell() {
        object PopularTagsTitle : SectionTitle(R.string.section_title_popular_tag)
        object NewArticlesTitle : SectionTitle(R.string.section_title_new_articles)
    }

    data class Tags(val tags: List<TagDto>) : NewArticleCell()

    data class NewArticle(
        val value: ArticleDto,
        val hasSourceCodeBlock: Boolean
    ) : NewArticleCell() {

        companion object {
            private const val USER_NAME_SEPARATOR = " "
        }

        /** 「ユーザーID ユーザー名」の表記で保持する */
        val userName = value.user.id + USER_NAME_SEPARATOR + value.user.name

        /** HTML形式のデータからテキスト形式に変換して保持する */
        val articleText = HtmlCompat
            .fromHtml(value.renderedBody.orEmpty(), HtmlCompat.FROM_HTML_MODE_COMPACT)
            .toString()
    }

    data class Error(val cause: Throwable) : NewArticleCell() {
        @StringRes
        val messageResId = when (cause) {
            is HttpException -> R.string.error_rate_limit_get_new_article
            else -> R.string.error_unknown
        }
    }

    override fun id(): String = when (this) {
        is NewArticle -> value.id
        else -> javaClass.simpleName
    }

    override fun viewType(): Int = when (this) {
        is Empty -> VIEW_TYPE_EMPTY
        is Loading -> VIEW_TYPE_LOADING
        is Error -> VIEW_TYPE_ERROR
        is Tags -> VIEW_TYPE_TAGS
        is NewArticle -> VIEW_TYPE_NEW_ARTICLE
        is SectionTitle -> VIEW_TYPE_SECTION_TITLE
    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_LOADING = 1
        const val VIEW_TYPE_ERROR = 2
        const val VIEW_TYPE_TAGS = 3
        const val VIEW_TYPE_NEW_ARTICLE = 4
        const val VIEW_TYPE_SECTION_TITLE = 5
    }
}