package com.lord_ukaka.worldnews.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lord_ukaka.worldnews.data.remote.dto.Source

@Entity(tableName = "article_table")
data class NewsArticle(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var saveArticle: Boolean = false,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: String?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var likes: Int = 0,
    val comments: Int = 0
)
