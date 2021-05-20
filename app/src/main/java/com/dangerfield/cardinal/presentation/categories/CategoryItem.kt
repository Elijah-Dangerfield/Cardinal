package com.dangerfield.cardinal.presentation.categories

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dangerfield.cardinal.R
import com.dangerfield.cardinal.databinding.ItemCategoryBinding
import com.dangerfield.cardinal.domain.model.Category
import com.xwray.groupie.viewbinding.BindableItem

class CategoryItem(val data: Category) : BindableItem<ItemCategoryBinding>() {
    override fun bind(viewBinding: ItemCategoryBinding, position: Int) {

        viewBinding.apply {
            categoryTitle.text = data.title

            Glide.with(categoryImage.context)
                .load(data.url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(categoryImage)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_category
    }

    override fun initializeViewBinding(view: View): ItemCategoryBinding {
        return ItemCategoryBinding.bind(view)
    }
}