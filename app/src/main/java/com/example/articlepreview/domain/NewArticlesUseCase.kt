package com.example.articlepreview.domain

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.example.articlepreview.core.extension.hasSourceCodeBlock
import com.example.articlepreview.core.extension.imageNodeURLs
import com.example.articlepreview.data.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.commonmark.parser.Parser
import javax.inject.Inject

data class NewArticle(
    /** 記事のタイトル */
    val title: String,
    /** 記事の本文 */
    val body: String?,
    /** 記事に関連するタグ一覧 */
    val tags: List<String>,
    /** 記事内の画像URL一覧 */
    val images: List<String>,
    /** 記事にソースコードが含まれているかどうか */
    val hasSourceCodeBlock: Boolean,
    /** 記事を書いたユーザー名 */
    val userName: String,
    /** ユーザーのプロフィール画像のURL */
    val userProfileURL: String?
)

class NewArticlesUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val parser: Parser
) {

    fun execute(page: Int): Flow<List<NewArticle>> =
        articleRepository.getArticles(page = page).map { articles ->
            articles.map { article ->
                val document = parser.parse(article.body)

                NewArticle(
                    title = article.title,
                    body = article.body?.let {
                        "${HtmlCompat.fromHtml(it, FROM_HTML_MODE_COMPACT)}"
                    },
                    tags = article.tags.map { it.name },
                    images = document.imageNodeURLs(),
                    hasSourceCodeBlock = document.hasSourceCodeBlock(),
                    userName = article.user.name,
                    userProfileURL = article.user.profileImageUrl
                )
            }
        }
}