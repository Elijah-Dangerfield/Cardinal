package com.dangerfield.cardinal.data.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ARTICLE_SIZE")
class ArticleSizeCacheEntity(
    @PrimaryKey var id: String,
    @ColumnInfo(name="size")  var size: String?
){
}