package com.example.articlepreview.util

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.requireDefaultItemAnimator() =
    itemAnimator as DefaultItemAnimator