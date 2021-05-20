package com.dangerfield.cardinal.presentation.ui.categories

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dangerfield.cardinal.R
import com.dangerfield.cardinal.databinding.ItemCategoryBinding
import com.dangerfield.cardinal.domain.model.Category
import com.dangerfield.cardinal.presentation.model.CategoryPresentationEntity
import com.dangerfield.cardinal.presentation.util.goneIf
import com.xwray.groupie.viewbinding.BindableItem

class CategoryItem(val data: CategoryPresentationEntity) : BindableItem<ItemCategoryBinding>() {

    override fun bind(viewBinding: ItemCategoryBinding, position: Int) {
        viewBinding.apply {
            categoryTitle.text = data.title
            selectedCover.goneIf(!data.isSelected)

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