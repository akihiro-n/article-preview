package com.example.articlepreview.presentation.new_article

import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.articlepreview.util.hasSourceCodeBlock
import com.example.articlepreview.data.ArticleRepository
import com.example.articlepreview.data.TagRepository
import com.example.articlepreview.data.model.ArticleDto
import com.example.articlepreview.data.model.TagDto
import com.example.articlepreview.presentation.new_article.model.NewArticleCell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import org.commonmark.parser.Parser
import javax.inject.Inject

@HiltViewModel
class NewArticlesViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val tagRepository: TagRepository,
    private val parser: Parser
) : ViewModel() {

    data class UiState(
        val cells: List<NewArticleCell> = emptyList(),
        val pagingError: Throwable? = null,
        val currentPage: Int = 1
    )

    private val _page = MutableStateFlow(1)
    private val _isLoading = MutableStateFlow(false)
    private val _pagingError = MutableStateFlow<Throwable?>(null)
    private val _otherError = MutableStateFlow<Throwable?>(null)
    private val _tags = MutableStateFlow<List<NewArticleCell>>(emptyList())
    private val _articles = MutableStateFlow<List<NewArticleCell>>(emptyList())
    private val _success = combine(_tags, _articles) { tag, article -> tag + article }

    private val _uiState: StateFlow<UiState> =
        combine(
            _page,
            _isLoading,
            _pagingError,
            _otherError,
            _success
        ) { page, loading, pagingError, otherError, success ->
            val isEmpty = loading.not() && success.isEmpty() && otherError == null

            return@combine UiState(
                cells = emptyList<NewArticleCell>().asSequence()
                    .plus(success)
                    .plusElement(NewArticleCell.Loading.takeIf { loading })
                    .plusElement(otherError?.let { NewArticleCell.Error(it) })
                    .plusElement(NewArticleCell.Empty.takeIf { isEmpty })
                    .filterNotNull().toList(),
                currentPage = page,
                pagingError = pagingError,
            )
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, UiState())

    val uiStateLiveData = _uiState.asLiveData(viewModelScope.coroutineContext)

    fun fetchNewArticles() {
        combine(
            tagRepository.getPopularTags().map { it.toCells() }.onEach { _tags.emit(it) },
            articleRepository.getArticles().map { it.toCells() }.onEach { _articles.emit(it) }
        ) { _, _ -> Unit }
            .onStart { _isLoading.emit(true) }
            .onEach {
                _page.emit(nextPage())
                _otherError.emit(null)
            }
            .catch { _otherError.emit(it) }
            .onCompletion { _isLoading.emit(false) }
            .launchIn(viewModelScope)
    }

    fun nextPageArticles() {
        articleRepository.getArticles(nextPage()).map { it.toCells() }
            .onStart { _isLoading.emit(true) }
            .onEach {
                _articles.emit(it)
                _page.emit(nextPage())
                _pagingError.emit(null)
            }
            .catch { _pagingError.emit(it) }
            .onCompletion { _isLoading.emit(false) }
            .launchIn(viewModelScope)
    }

    private fun currentUiState() = _uiState.value

    private fun nextPage() = currentUiState().currentPage + 1

    @JvmName("toCellsTagDto")
    private fun List<TagDto>.toCells() =
        listOf(
            NewArticleCell.SectionTitle.PopularTagsTitle,
            NewArticleCell.Tags(this)
        )

    private fun List<ArticleDto>.toCells() =
        listOf(NewArticleCell.SectionTitle.NewArticlesTitle) + map {
            NewArticleCell.NewArticle(
                value = it,
                articleText = HtmlCompat.fromHtml(
                    it.renderedBody.orEmpty(),
                    FROM_HTML_MODE_COMPACT
                ).toString(),
                hasSourceCodeBlock = parser.parse(it.body).hasSourceCodeBlock()
            )
        }
}