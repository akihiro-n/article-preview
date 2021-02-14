package com.example.articlepreview.presentation.new_article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.articlepreview.data.model.TagDto
import com.example.articlepreview.databinding.*
import com.example.articlepreview.presentation.new_article.model.NewArticleCell
import com.example.articlepreview.util.ComparableListItem
import com.squareup.picasso.Picasso

class PopularTagsViewHolder(
    val binding: ViewHolderPopularTagsBinding
) : RecyclerView.ViewHolder(binding.root)

class SectionTitleViewHolder(
    val binding: ViewHolderSectionTitleBinding
) : RecyclerView.ViewHolder(binding.root)

class ArticleViewHolder(
    val binding: ViewHolderArticleBinding
) : RecyclerView.ViewHolder(binding.root)

class ProgressBarViewHolder(
    val binding: ViewHolderProgressBarBinding
) : RecyclerView.ViewHolder(binding.root)

class NewArticlesAdapter : ListAdapter<NewArticleCell, RecyclerView.ViewHolder>(
    ComparableListItem.diffUtilItemCallback()
) {

    private val tagsAdapter by lazy { TagsAdapter() }

    override fun getItemViewType(position: Int) = getItem(position).viewType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NewArticleCell.VIEW_TYPE_SECTION_TITLE -> SectionTitleViewHolder(
                binding = ViewHolderSectionTitleBinding.inflate(inflater, parent, false)
            )
            NewArticleCell.VIEW_TYPE_TAGS -> PopularTagsViewHolder(
                binding = ViewHolderPopularTagsBinding.inflate(inflater, parent, false)
            )
            NewArticleCell.VIEW_TYPE_NEW_ARTICLE -> ArticleViewHolder(
                binding = ViewHolderArticleBinding.inflate(inflater, parent, false)
            )
            NewArticleCell.VIEW_TYPE_LOADING -> ProgressBarViewHolder(
                binding = ViewHolderProgressBarBinding.inflate(inflater, parent, false)
            )
            // TODO: 別のViewHolderの実装
            else -> SectionTitleViewHolder(
                binding = ViewHolderSectionTitleBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            item is NewArticleCell.Tags && holder is PopularTagsViewHolder -> {
                holder.binding.tagList.adapter = tagsAdapter.also { it.submitList(item.tags) }
            }
            item is NewArticleCell.SectionTitle && holder is SectionTitleViewHolder -> {
                holder.binding.title.text = holder.binding.root.context.getString(item.titleResId)
            }
            item is NewArticleCell.NewArticle && holder is ArticleViewHolder -> {
                holder.binding.article = item
                // TODO: BindingAdapterを作成してDataBindingで画像を渡せるよう修正する
                Picasso.get().load(item.value.user.profileImageUrl)
                    .resize(60,60)
                    .centerCrop()
                    .into(holder.binding.userImage)
            }
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
            Picasso.get().load(item.iconUrl)
                .resize(40, 40)
                .centerCrop()
                .into(tagImage)
        }
    }
}