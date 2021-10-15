package com.dangerfield.cardinal.data.network.model

import com.google.gson.annotations.SerializedName
import java.security.MessageDigest

data class ArticleNetworkEntity(
    @SerializedName("author") val author: String? = "",
    @SerializedName("content") val content: String? = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("publishedAt") val publishedAt: String? = "",
    @SerializedName("source") val sourceNetworkEntity: SourceNetworkEntity? = SourceNetworkEntity(
        "",
        ""
    ),
    @SerializedName("title") val title: String? = "",
    @SerializedName("url") val url: String? = "",
    @SerializedName("urlToImage") val urlToImage: String? = "",
) {
    val id : String by lazy { "$url-$title-$publishedAt".sha256() }
}


private fun String.sha256(): String {
    return MessageDigest
        .getInstance("SHA-256")
        .digest(this.toByteArray())
        .fold("", { str, it -> str + "%02x".format(it) })
}