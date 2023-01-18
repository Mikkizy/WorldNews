package com.lord_ukaka.worldnews.data.repository

import android.content.res.Resources
import androidx.paging.*
import com.lord_ukaka.worldnews.core.sealed.DataResponse
import com.lord_ukaka.worldnews.core.sealed.Error
import com.lord_ukaka.worldnews.core.utils.Constants.ITEMS_PER_PAGE
import com.lord_ukaka.worldnews.data.local.NewsDatabase
import com.lord_ukaka.worldnews.data.paging.NewsRemoteMediator
import com.lord_ukaka.worldnews.data.paging.SearchNewsPagingSource
import com.lord_ukaka.worldnews.data.remote.NewsApi
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.models.User
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class NewsRepositoryImpl @Inject constructor(
    private val database: NewsDatabase,
    private val api: NewsApi
): NewsRepository {

    private val dao = database.dao

    override fun getTopHeadlines(
        countryCode: String, category: String
    ): Flow<DataResponse<PagingData<NewsArticle>>> = flow{
        emit(DataResponse.Loading())
        val pagingSourceFactory = { database.dao.getAllNews() }
        try {
            val pagerArticles = Pager(
                config = PagingConfig(pageSize = ITEMS_PER_PAGE),
                remoteMediator = NewsRemoteMediator(
                    api = api,
                    database = database
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow
            pagerArticles.collectLatest {
                emit(DataResponse.Success(it))
            }
        } catch (error: Exception) {
            val errorMessageInt = when (error) {
                is HttpException -> Error.Empty.message
                is IOException -> Error.Network.message
                else -> Error.Unknown.message
            }
            val errorMessage = Resources.getSystem().getString(errorMessageInt)
            emit(DataResponse.Error(errorMessage))
        }
    }

    override suspend fun saveAllNews(articles: PagingData<NewsArticle>) {
        val list = mutableListOf<NewsArticle>()
        articles.map {
            list.add(it)
        }
        list.forEach {
            dao.saveNews(it)
        }
    }

    override fun searchForNews(query: String): Flow<DataResponse<PagingData<NewsArticle>>> = flow {
        emit(DataResponse.Loading())
        val pagingSourceFactory = { SearchNewsPagingSource(api, query)}
        try {
            val results = Pager(
                config = PagingConfig(pageSize = ITEMS_PER_PAGE),
                pagingSourceFactory = pagingSourceFactory
            ).flow
            results.collectLatest {
                emit(DataResponse.Success(it))
            }
        } catch (e: Exception) {
            val errorMessageInt = when(e) {
                is HttpException -> Error.Empty.message
                is IOException -> Error.Network.message
                else -> Error.Unknown.message
            }
            val errorMessage = Resources.getSystem().getString(errorMessageInt)
            emit(DataResponse.Error(errorMessage))
        }
    }

    override fun getSavedNews(): Flow<List<NewsArticle>> = flow {
        emit(dao.getSavedNews())
    }

    override suspend fun saveNews(article: NewsArticle) {
        val newsArticle = article.copy(
            saveArticle = true
        )
        return dao.saveNews(newsArticle)
    }

    override suspend fun deleteNews(article: NewsArticle) {
       return dao.deleteNews(article)
    }

    override suspend fun deleteAllNews() {
        return dao.deleteNewsArticles()
    }

    override suspend fun getNewsArticleById(id: Int): NewsArticle? {
        return dao.getNewsArticle(id)
    }

    override suspend fun signInUser(email: String?, password: String?): DataResponse<User?> {
        return dao.signIn(email, password)?.let {
           DataResponse.Success(data = it)
        } ?: DataResponse.Error(message = Resources.getSystem().getString(Error.Empty.message))
    }

    override fun getAllUsers(): Flow<List<User>> {
        return dao.getAllUsers()
    }

    override suspend fun getUser(id: Int): User? {
       return dao.getUser(id)
    }

    override suspend fun insertUser(user: User) {
        return dao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        return dao.deleteUser(user)
    }
}