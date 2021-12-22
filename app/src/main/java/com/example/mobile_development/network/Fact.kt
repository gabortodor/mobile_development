package com.example.mobile_development.network

import com.squareup.moshi.Json

data class Fact(
    val id: String,
    val text: String,
    val source:String,
    @Json(name="source_url")
    val sourceUrl:String,
    val language:String,
    val permalink:String)