package com.lord_ukaka.worldnews.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lord_ukaka.worldnews.data.remote.NewsApi
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import javax.inject.Inject

class SearchNewsPagingSource @Inject constructor(
    private val newsApi: NewsApi,
    private val query: String
): PagingSource<Int, NewsArticle>() {
    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val currentPage = params.key ?: 1
        return try {
            val response = newsApi.SearchForNews(searchQuery = query, pageNumber = currentPage).map {
                it.toNewsArticle()
            }
            val endOfPaginationReached = response.isEmpty()
            if (response.isNotEmpty()) {
                LoadResult.Page(
                    data = response,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (endOfPaginationReached) null else currentPage + 1
                )
            } else {
                LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}