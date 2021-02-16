package com.example.articlepreview.presentation.new_article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.articlepreview.data.model.TagDto
import com.example.articlepreview.databinding.ViewHolderPopularTagBinding
import com.squareup.picasso.Picasso

class TagAdapter : ListAdapter<TagDto, TagAdapter.PopularTagViewHolder>(
    object : DiffUtil.ItemCallback<TagDto>() {
        override fun areItemsTheSame(oldItem: TagDto, newItem: TagDto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TagDto, newItem: TagDto): Boolean =
            oldItem == newItem
    }
) {

    class PopularTagViewHolder(
        val binding: ViewHolderPopularTagBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularTagViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PopularTagViewHolder(
            binding = ViewHolderPopularTagBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularTagViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            tag = item
            // TODO: BindingAdapterを作成してDataBindingで画像を渡せるよう修正する
            Picasso.get().load(item.iconUrl)
                .noFade()
                .fit()
                .centerCrop()
                .into(holder.binding.tagImage)
        }
    }
}