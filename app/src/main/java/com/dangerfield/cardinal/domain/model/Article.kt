package com.dangerfield.cardinal.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val id: String,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Parcelable {
    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        Source(""),
        "",
        "",
        "")
}