package com.musasyihab.simplenews.model

data class ArticleModel(
        val source: ArticleSourceModel,
        val author: String,
        val title: String,
        val description: String,
        val url: String,
        val urlToImage: String,
        val publishedAt: String,
        val content: String
)