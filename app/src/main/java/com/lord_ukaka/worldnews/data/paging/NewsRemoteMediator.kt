package com.lord_ukaka.worldnews.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lord_ukaka.worldnews.data.local.NewsDatabase
import com.lord_ukaka.worldnews.data.remote.NewsApi
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.models.NewsRemoteKeys
import javax.inject.Inject

@ExperimentalPagingApi
class NewsRemoteMediator @Inject constructor(
    private val api: NewsApi,
    private val database: NewsDatabase
): RemoteMediator<Int, NewsArticle>() {

    private val dao = database.dao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsArticle>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response = api.getTopHeadlines(pageNumber = currentPage)
            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                   dao.deleteNewsArticles()
                    dao.deleteAllRemoteKeys()
                }
                val keys = response.map { article ->
                   NewsRemoteKeys(
                        id = article.toNewsArticle().id!!.toString(),
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                dao.addAllRemoteKeys(remoteKeys = keys)
                dao.addNewsArticles(response.map {
                    it.toNewsArticle()
                })
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, NewsArticle>
    ): NewsRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                dao.getRemoteKeys(id = id.toString())
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, NewsArticle>
    ): NewsRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { newsArticle ->
                dao.getRemoteKeys(id = newsArticle.id!!.toString())
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int,NewsArticle>
    ): NewsRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { newsArticle ->
                dao.getRemoteKeys(id = newsArticle.id!!.toString())
            }
    }
}