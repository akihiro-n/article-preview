package com.example.articlepreview.presentation.new_article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.articlepreview.databinding.FragmentNewArticlesBinding
import com.example.articlepreview.presentation.new_article.model.NewArticleCell
import dagger.hilt.android.AndroidEntryPoint

@Suppress("COMPATIBILITY_WARNING")
@AndroidEntryPoint
class NewArticlesFragment : Fragment() {

    private val viewModel by viewModels<NewArticlesViewModel>()
    private val articlesAdapter by lazy { NewArticlesAdapter() }

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
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->
            with(binding.newArticleList) {
                recycledViewPool.setMaxRecycledViews(
                    NewArticleCell.VIEW_TYPE_TAGS,
                    state.cells.filterIsInstance<NewArticleCell.Tags>().count()
                )
                adapter = articlesAdapter.also { it.submitList(state.cells) }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}