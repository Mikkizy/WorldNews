package com.lord_ukaka.worldnews.data.remote.dto

import com.lord_ukaka.worldnews.domain.models.NewsArticle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("articles")
data class Article(
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) {
    fun toNewsArticle(): NewsArticle {
        return NewsArticle(
            author = author,
            content = content,
            description = description,
            publishedAt = publishedAt,
            source = source?.name,
            title = title,
            url = url,
            urlToImage = urlToImage
        )
    }
}