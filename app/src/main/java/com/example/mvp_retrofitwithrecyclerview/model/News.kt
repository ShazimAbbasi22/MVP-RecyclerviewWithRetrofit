package com.example.mvp_retrofitwithrecyclerview.model

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)