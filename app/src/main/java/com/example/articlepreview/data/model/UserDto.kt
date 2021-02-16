package com.example.articlepreview.data.model

import com.squareup.moshi.Json

data class UserDto(
    val id: String,
    val name: String,
    val description: String?,
    @Json(name = "profile_image_url") val profileImageUrl: String?
)