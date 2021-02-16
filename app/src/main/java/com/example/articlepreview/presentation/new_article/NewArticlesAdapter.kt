package com.example.articlepreview.presentation.new_article

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.articlepreview.data.model.TagDto
import com.example.articlepreview.databinding.*
import com.example.articlepreview.presentation.new_article.model.NewArticleCell
import com.example.articlepreview.util.ComparableListItem
import com.example.articlepreview.util.StatefulAdapter
import com.example.articlepreview.util.requireDefaultItemAnimator
import com.squareup.picasso.Picasso

class NewArticlesAdapter : StatefulAdapter<NewArticleCell, RecyclerView.ViewHolder>(
    ComparableListItem.diffUtilItemCallback()
) {

    var onClickArticle: (NewArticleCell.NewArticle) -> Unit = {}
    var onClickTag: (TagDto) -> Unit = {}

    private val tagsAdapter = TagAdapter()
    private val tagsRecyclerViewPool = RecyclerView.RecycledViewPool()

    override fun getItemViewType(position: Int) = getItem(position).viewType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NewArticleCell.VIEW_TYPE_SECTION_TITLE -> SectionTitleViewHolder(
                binding = ViewHolderSectionTitleBinding.inflate(inflater, parent, false)
            )
            NewArticleCell.VIEW_TYPE_TAGS -> PopularTagsViewHolder(
                binding = ViewHolderPopularTagsBinding.inflate(inflater, parent, false)
            ).apply {
                binding.tagList.setRecycledViewPool(tagsRecyclerViewPool)
            }
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
                with(holder.binding.tagList) {
                    requireDefaultItemAnimator().supportsChangeAnimations = false
                    swapAdapter(
                        tagsAdapter.also { it.submitList(item.tags) },
                        false
                    )
                    setItemViewCacheSize(item.tags.count())
                    tagsAdapter.onClickTag = onClickTag
                }
            }
            item is NewArticleCell.SectionTitle && holder is SectionTitleViewHolder -> {
                holder.binding.title.text = holder.binding.root.context.getString(item.titleResId)
            }
            item is NewArticleCell.NewArticle && holder is ArticleViewHolder -> {
                with(holder.binding) {
                    article = item
                    // TODO: BindingAdapterを作成してDataBindingで画像を渡せるよう修正する
                    Picasso.get().load(item.value.user.profileImageUrl)
                        .noFade()
                        .fit()
                        .centerCrop()
                        .into(userImage)
                    root.setOnClickListener { onClickArticle.invoke(item) }
                }
            }
        }
    }

    class PopularTagsViewHolder(
        val binding: ViewHolderPopularTagsBinding
    ) : StatefulAdapter.ViewHolder(binding.root) {

        override fun key(): String = javaClass.simpleName

        override fun savedScrollState(): Parcelable? =
            binding.tagList.layoutManager?.onSaveInstanceState()

        override fun restoreState(scrollState: Parcelable) {
            binding.tagList.layoutManager?.onRestoreInstanceState(scrollState)
        }
    }

    class SectionTitleViewHolder(val binding: ViewHolderSectionTitleBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ArticleViewHolder(val binding: ViewHolderArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ProgressBarViewHolder(val binding: ViewHolderProgressBarBinding) :
        RecyclerView.ViewHolder(binding.root)
}
