package com.example.articlepreview.presentation.new_article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.articlepreview.databinding.FragmentNewArticlesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewArticlesFragment : Fragment() {

    private val viewModel by viewModels<NewArticlesViewModel>()
    private lateinit var binding: FragmentNewArticlesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel
        super.onViewCreated(view, savedInstanceState)
    }
}