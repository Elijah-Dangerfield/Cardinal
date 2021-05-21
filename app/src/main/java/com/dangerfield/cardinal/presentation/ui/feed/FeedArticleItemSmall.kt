package com.dangerfield.cardinal.presentation.ui.feed

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dangerfield.cardinal.R
import com.dangerfield.cardinal.databinding.ItemFeedArticleSmallBinding
import com.dangerfield.cardinal.domain.model.Article
import com.dangerfield.cardinal.presentation.model.ArticlePresentationEntity
import com.dangerfield.cardinal.presentation.util.visibleIf
import com.xwray.groupie.viewbinding.BindableItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class FeedArticleItemSmall(val data: ArticlePresentationEntity) : BindableItem<ItemFeedArticleSmallBinding>(), FeedArticleItem {
    override fun bind(viewBinding: ItemFeedArticleSmallBinding, position: Int) {
        viewBinding.apply {
            articleTitle.text = data.title ?: "Untitled"
            articlePublishDate.text = data.publishedAt?.toReadableDate() ?: "No date"
            articlePublisher.text = data.source?.name
            articlePreview.text = data.description ?: "Click to read more"

            imageCard.visibleIf(data.urlToImage != null)

            data.urlToImage?.let {
                Glide.with(articleImage.context)
                    .load(data.urlToImage)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(articleImage)
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_feed_article_small
    }

    override fun initializeViewBinding(view: View): ItemFeedArticleSmallBinding {
        return ItemFeedArticleSmallBinding.bind(view)
    }

    /**
     * converts time stamp given by api to a readable format
     */
    private fun String.toReadableDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        var date: Date? = null
        try {
            date = formatter.parse(this)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date.toString().dropLast(18)
    }

    override fun getArticle(): ArticlePresentationEntity {
        return data
    }
}