package com.musasyihab.simplenews.model

data class GetNewsModel(val status: String, val totalResults: Int, val articles: List<ArticleModel>)