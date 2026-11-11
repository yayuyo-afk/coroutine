package com.example.coroutine.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val id: String,
    @SerialName("img_src")
    val imgSrc: String
)
