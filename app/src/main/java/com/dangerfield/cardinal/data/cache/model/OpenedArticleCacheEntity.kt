package com.dangerfield.cardinal.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "OPENED_ARTICLE")
data class OpenedArticleCacheEntity(
    @PrimaryKey var id: String,
    @ColumnInfo(name="author")  var author: String?,
    @ColumnInfo(name="content") var content: String? = "",
    @ColumnInfo(name="description") var description: String? = "",
    @ColumnInfo(name="publishedAt")var publishedAt: String? = "",
    @ColumnInfo(name="sourceName") var sourceName: String? = "",
    @ColumnInfo(name="sourceId") var sourceId: String? = "",
    @ColumnInfo(name="title") var title: String? = "",
    @ColumnInfo(name="url") var url: String? = "",
    @ColumnInfo(name="urlToImage") var urlToImage: String? = "",
) {
}