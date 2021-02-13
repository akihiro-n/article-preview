package com.example.articlepreview.data.model

import com.squareup.moshi.Json

data class TagDto(val id: String, @Json(name = "icon_url") val iconUrl: String)