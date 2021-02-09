package com.example.articlepreview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.articlepreview.databinding.FragmentNewArticlesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewArticlesFragment : Fragment() {

    private lateinit var binding: FragmentNewArticlesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }
}