package com.example.articlepreview.domain

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.example.articlepreview.core.extension.hasSourceCodeBlock
import com.example.articlepreview.core.extension.imageNodeURLs
import com.example.articlepreview.data.ArticleRepository
import com.example.articlepreview.domain.model.NewArticle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.commonmark.parser.Parser
import javax.inject.Inject

class NewArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository,
    private val parser: Parser
) {

    fun execute(page: Int): Flow<List<NewArticle>> =
        repository.getArticles(page = page).map { articles ->
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