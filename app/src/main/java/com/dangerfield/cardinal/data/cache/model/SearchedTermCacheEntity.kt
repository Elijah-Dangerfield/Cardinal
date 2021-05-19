package com.dangerfield.cardinal.data.cache.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "SEARCHED_TERM")
@Parcelize
data class SearchedTermCacheEntity(@PrimaryKey val value: String): Parcelable {
    constructor() : this("")
}