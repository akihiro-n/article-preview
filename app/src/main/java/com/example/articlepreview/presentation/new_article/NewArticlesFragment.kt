package com.example.articlepreview.presentation.new_article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.articlepreview.databinding.FragmentNewArticlesBinding
import com.example.articlepreview.presentation.new_article.model.NewArticleCell
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@Suppress("COMPATIBILITY_WARNING")
@AndroidEntryPoint
class NewArticlesFragment : Fragment() {

    companion object {
        private const val NEW_ARTICLE_LIST_PREFETCH_COUNT = 10
    }

    private val viewModel by viewModels<NewArticlesViewModel>()

    private val articlesAdapter by lazy {
        NewArticlesAdapter().apply {
            onClickArticle = {
                // TODO: 詳細画面へ遷移する
                showSnackBar(message = "TODO: 詳細画面へ遷移する $it")
            }
            onClickTag = {
                // TODO: タグを元に一覧画面へ遷移する
                showSnackBar(message = "TODO: タグを元に一覧画面へ遷移する $it")
            }
        }
    }

    private lateinit var binding: FragmentNewArticlesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.fetchNewArticles()
        binding = FragmentNewArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding.newArticleList) {
            with(layoutManager as LinearLayoutManager) {
                isItemPrefetchEnabled = true
                initialPrefetchItemCount = NEW_ARTICLE_LIST_PREFETCH_COUNT
            }
            setItemViewCacheSize(NEW_ARTICLE_LIST_PREFETCH_COUNT)
            recycledViewPool.setMaxRecycledViews(NewArticleCell.VIEW_TYPE_TAGS, 0)
            adapter = articlesAdapter
        }
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->
            articlesAdapter.submitList(state.cells)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}