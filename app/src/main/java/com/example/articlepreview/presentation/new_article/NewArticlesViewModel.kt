package com.example.articlepreview.presentation.new_article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.articlepreview.data.ArticleRepository
import com.example.articlepreview.data.TagRepository
import com.example.articlepreview.data.model.ArticleDto
import com.example.articlepreview.presentation.new_article.model.NewArticleCell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NewArticlesViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    data class UiState(
        val cells: List<NewArticleCell> = emptyList(),
        val currentPage: Int = 0
    ) {

        companion object {

            fun loading() = UiState(cells = listOf(NewArticleCell.Loading))
            fun resultEmpty() = UiState(cells = listOf(NewArticleCell.Empty))
            fun error(cause: Throwable) = UiState(cells = listOf(NewArticleCell.Error(cause)))

            fun success(
                tag: NewArticleCell.Tags,
                articles: List<NewArticleCell.NewArticle>,
                currentPage: Int
            ) = UiState(
                cells = listOf(tag) + articles,
                currentPage = currentPage
            )
        }
    }

    private val _currentPage = MutableStateFlow(1)
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<Throwable?>(null)
    private val _tag = MutableStateFlow<NewArticleCell.Tags?>(null)
    private val _articles = MutableStateFlow<List<NewArticleCell.NewArticle>>(emptyList())

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiStateLiveData = _uiState.asLiveData(viewModelScope.coroutineContext)

    init {
        combine(
            _currentPage,
            _isLoading,
            _error,
            _articles,
            _tag
        ) { currentPage, loading, error, articles, tag ->
            when {
                loading -> UiState.loading()
                error != null -> UiState.error(error)
                articles.isNotEmpty() && tag != null -> UiState.success(tag, articles, currentPage)
                else -> UiState.resultEmpty()
            }
        }
            .distinctUntilChanged()
            .onEach { _uiState.emit(it) }
            .launchIn(viewModelScope)
    }

    fun getNewArticle() {
        val tags = tagRepository.getPopularTags()
            .map { dto -> dto.map { NewArticleCell.Tags(dto) } }
        val articles =

            combine(
                tagRepository.getPopularTags(),
                articleRepository.getArticles(page = _currentPage.value)
            ) { tagDto, articleDto ->

            }
    }

    private fun popularTags() =
        tagRepository.getPopularTags().map { dto ->
            dto.map { NewArticleCell.Tags(dto) }
        }

    fun nextArticles() =
        articleRepository.getArticles(page = _currentPage.value)
            .map { response: List<ArticleDto> ->
                response.map { NewArticleCell.NewArticle(it, false) }
            }
}