package com.example.articlepreview.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * ListAdapterでアイテムを比較をシンプルに扱うための型定義
 */
interface ComparableListItem {

    fun id(): String
    fun viewType(): Int

    companion object {

        fun <T : ComparableListItem> diffUtilItemCallback() = object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
                oldItem.id() == newItem.id()

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
                oldItem == newItem
        }
    }
}