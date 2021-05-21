package com.dangerfield.cardinal.presentation.model

import android.os.Parcelable
import com.dangerfield.cardinal.domain.model.Source
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticlePresentationEntity(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var displaySize: DisplaySize
) : Parcelable {
    constructor() : this(
        "",
        "",
        "",
        "",
        Source(""),
        "",
        "",
        "",
        DisplaySize.Large
    )
}

enum class DisplaySize { Large, Small }