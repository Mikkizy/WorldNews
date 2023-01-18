package com.lord_ukaka.worldnews.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_remote_keys_table")
data class NewsRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
