package com.lord_ukaka.worldnews.domain.repository

import androidx.paging.PagingData
import com.lord_ukaka.worldnews.core.sealed.DataResponse
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.models.User
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getTopHeadlines(countryCode: String, category: String): Flow<DataResponse<PagingData<NewsArticle>>>

    fun searchForNews(query: String): Flow<DataResponse<PagingData<NewsArticle>>>

    fun getSavedNews(): Flow<List<NewsArticle>>

    suspend fun getNewsArticleById(id: Int): NewsArticle?

    suspend fun saveNews(article: NewsArticle)

    suspend fun saveAllNews(articles: PagingData<NewsArticle>)

    suspend fun deleteNews(article: NewsArticle)

    suspend fun deleteAllNews()

    suspend fun signInUser(email: String?, password: String?): DataResponse<User?>

    fun getAllUsers(): Flow<List<User>>

    suspend fun getUser(id: Int): User?

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)
}