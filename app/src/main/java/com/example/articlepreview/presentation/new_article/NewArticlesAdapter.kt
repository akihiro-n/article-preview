package com.example.articlepreview.presentation.new_article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.articlepreview.data.model.TagDto
import com.example.articlepreview.databinding.ViewHolderPopularTagBinding
import com.example.articlepreview.databinding.ViewHolderPopularTagsBinding
import com.example.articlepreview.presentation.new_article.model.NewArticleCell
import com.example.articlepreview.util.ComparableListItem
import com.squareup.picasso.Picasso

class PopularTagsViewHolder(
    val binding: ViewHolderPopularTagsBinding
) : RecyclerView.ViewHolder(binding.root)

class NewArticlesAdapter : ListAdapter<NewArticleCell, RecyclerView.ViewHolder>(
    ComparableListItem.diffUtilItemCallback()
) {

    private val tagsAdapter by lazy { TagsAdapter() }

    override fun getItemViewType(position: Int) = getItem(position).viewType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PopularTagsViewHolder(
            binding = ViewHolderPopularTagsBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item is NewArticleCell.Tags && holder is PopularTagsViewHolder) {
            holder.binding.tagList.adapter = tagsAdapter.also { it.submitList(item.tags) }
        }
    }
}

class PopularTagViewHolder(
    val binding: ViewHolderPopularTagBinding
) : RecyclerView.ViewHolder(binding.root)

class TagsAdapter : ListAdapter<TagDto, PopularTagViewHolder>(
    object : DiffUtil.ItemCallback<TagDto>() {
        override fun areItemsTheSame(oldItem: TagDto, newItem: TagDto): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TagDto, newItem: TagDto): Boolean =
            oldItem == newItem
    }
) {

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
            Picasso.get().load(item.iconUrl).into(tagImage)
        }
    }
}