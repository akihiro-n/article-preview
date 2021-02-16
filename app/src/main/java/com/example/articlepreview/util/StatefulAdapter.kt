package com.example.articlepreview.util

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * ネストしたRecyclerViewのスクロール状態を内部で管理するAdapter
 * [StatefulAdapter.ViewHolder]を実装したViewHolder内のスクロールの状態を保存し復元する
 * Fixme: もう少し使いやすい実装にしたい。。。
 */
abstract class StatefulAdapter<T, VH : RecyclerView.ViewHolder>(
    itemCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(itemCallback) {

    /** スクロールの状態を保存する用途のViewHolder */
    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /** ViewHolderをユニークに識別するKey */
        abstract fun key(): String

        /** スクロールの状態を保存 */
        abstract fun savedScrollState(): Parcelable?

        /** スクロールの状態を復元できるタイミングで呼ばれる */
        abstract fun restoreState(scrollState: Parcelable)
    }

    /** ViewHolder内のスクロールの状態を保持する */
    private val viewHoldersScrollStates by lazy {
        mutableMapOf<String, Parcelable?>()
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        if (holder is ViewHolder) {
            viewHoldersScrollStates[holder.key()] = holder.savedScrollState()
        }
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: VH) {
        if (holder is ViewHolder) {
            val scrollState = viewHoldersScrollStates[holder.key()]
            scrollState?.let { holder.restoreState(it) }
        }
        super.onViewAttachedToWindow(holder)
    }
}