package com.example.articlepreview.domain

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import com.example.articlepreview.core.extension.hasSourceCodeBlock
import com.example.articlepreview.core.extension.imageNodes
import com.example.articlepreview.data.ArticleRepository
import com.example.articlepreview.data.model.ArticleDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.commonmark.parser.Parser
import javax.inject.Inject

data class NewArticle(
    val title: String,
    val planeBody: String,
    val markdownBody: String,
    val tags: List<String>,
    val hasSourceCodeBlock: Boolean,
    val userImage: String,
    val images: List<String>
)

class NewArticlesUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val parser: Parser
) {

    fun execute(page: Int): Flow<List<NewArticle>> =
        articleRepository
            .getArticles(page = page)
            .map { articles ->
                articles.map { it.toState() }
            }

    private fun ArticleDto.toState(): NewArticle {

        val document = parser.parse(body)

        return NewArticle(
            title = title,
            planeBody = "${HtmlCompat.fromHtml(body, FROM_HTML_MODE_COMPACT)}",
            tags = tags.map { it.name },
            markdownBody = body,
            hasSourceCodeBlock = document.hasSourceCodeBlock(),
            images = document.imageNodes().map { it.destination },
            userImage = ""
        )
    }
}