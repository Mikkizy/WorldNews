package com.lord_ukaka.worldnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.models.NewsRemoteKeys
import com.lord_ukaka.worldnews.domain.models.User

@Database(
    entities = [
        NewsArticle::class,
        User::class,
        NewsRemoteKeys::class
    ],
    version = 1
)
abstract class NewsDatabase: RoomDatabase() {

    abstract val dao: NewsArticleDao
}