package com.dangerfield.cardinal.data.network.model

import com.google.gson.annotations.SerializedName

data class SourceNetworkEntity(
    @SerializedName("id") val id: String? = "",
    @SerializedName("name") val name: String? = ""
)