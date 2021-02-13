package com.example.articlepreview.presentation.new_article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.articlepreview.core.extension.hasSourceCodeBlock
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
        /**
         * RecyclerViewに表示するデータ
         */
        val cells: List<NewArticleCell> = emptyList(),

        /**
         * ページング中に発生したエラー
         */
        val pagingError: Throwable? = null,

        /**
         * 記事一覧取得APIにおける現在のページ数
         */
        val currentPage: Int = 0
    )

    private val _page = MutableStateFlow(1)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<Throwable?>(null)
    private val _success = MutableStateFlow<List<NewArticleCell>>(emptyList())

    private val _uiState: StateFlow<UiState> =
        combine(_page, _isLoading, _error, _success) { page, loading, error, success ->

            val hasArticles = success.filterIsInstance<NewArticleCell.NewArticle>().isNotEmpty()
            val isEmpty = loading.not() && hasArticles.not() && error == null
            val pagingError = error?.takeIf { hasArticles }
            val otherError = error?.takeUnless { hasArticles }
            val articles = success.takeIf { hasArticles }.orEmpty()

            val newCells = emptyList<NewArticleCell>().asSequence()
                .plus(articles)
                .plusElement(NewArticleCell.Loading.takeIf { loading })
                .plusElement(otherError?.let { NewArticleCell.Error(it) })
                .plusElement(NewArticleCell.Empty.takeIf { isEmpty })
                .filterNotNull()

            return@combine UiState(
                cells = newCells.toList(),
                currentPage = if (hasArticles) page + 1 else page,
                pagingError = pagingError,
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState())

    val uiStateLiveData = _uiState.asLiveData(viewModelScope.coroutineContext)

    init {
        combine(
            tagRepository.getPopularTags().map { it.toCell() },
            articleRepository.getArticles().map { it.toCells() }
        ) { tag, articles -> listOf(tag) + articles }
            .onStart { _isLoading.emit(true) }
            .onEach {
                _success.emit(it)
                _error.emit(null)
            }
            .catch { _error.emit(it) }
            .onCompletion { _isLoading.emit(false) }
            .launchIn(viewModelScope)
    }

    fun getNextPageArticles() {
        articleRepository.getArticles(nextPage())
            .map { it.toCells() }
            .onStart { _isLoading.emit(true) }
            .onEach {
                _success.emit(it)
                _error.emit(null)
            }
            .catch { _error.emit(it) }
            .onCompletion { _isLoading.emit(false) }
            .launchIn(viewModelScope)
    }

    private fun currentUiState() = _uiState.value

    private fun nextPage() = _uiState.value.currentPage + 1

    private fun List<TagDto>.toCell() = NewArticleCell.Tags(this)

    private fun List<ArticleDto>.toCells() = map {
        NewArticleCell.NewArticle(
            value = it,
            hasSourceCodeBlock = parser.parse(it.body).hasSourceCodeBlock()
        )
    }
}