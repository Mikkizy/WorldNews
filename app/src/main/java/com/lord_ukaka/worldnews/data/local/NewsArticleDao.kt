package com.lord_ukaka.worldnews.data.local

import androidx.paging.PagingSource
import androidx.room.*
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.models.NewsRemoteKeys
import com.lord_ukaka.worldnews.domain.models.User
import kotlinx.coroutines.flow.Flow
@Dao
interface NewsArticleDao {
    //News Article
    @Transaction
    @Query("SELECT * FROM article_table")
    fun getAllNews(): PagingSource<Int, NewsArticle>

    @Query("SELECT * FROM article_table WHERE id = :id")
    suspend fun getNewsArticle(id: Int): NewsArticle?

    @Transaction
    @Query("SELECT * FROM article_table WHERE saveArticle = 1")
    fun getSavedNews(): List<NewsArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNews(article: NewsArticle)

    @Delete
    suspend fun deleteNews(article: NewsArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewsArticles(articles: List<NewsArticle>)

    @Query("DELETE FROM article_table WHERE saveArticle = 0")
    suspend fun deleteNewsArticles()


    //User
    @Transaction
    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    suspend fun signIn(email: String?, password: String?): User?

    @Transaction
    @Query("SELECT * FROM user WHERE userId = :id")
    suspend fun getUser(id: Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    //Remote Keys
    @Transaction
    @Query("SELECT * FROM news_remote_keys_table WHERE id =:id")
    suspend fun getRemoteKeys(id: String): NewsRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<NewsRemoteKeys>)

    @Query("DELETE FROM news_remote_keys_table")
    suspend fun deleteAllRemoteKeys()
}