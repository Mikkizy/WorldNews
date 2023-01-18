package com.lord_ukaka.worldnews.domain.usecases

import androidx.paging.PagingData
import com.lord_ukaka.worldnews.core.sealed.DataResponse
import com.lord_ukaka.worldnews.data.remote.dto.NewsResponse
import com.lord_ukaka.worldnews.domain.models.NewsArticle
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    operator fun invoke(
        countryCode: String,
        category: String,
    ): Flow<DataResponse<PagingData<NewsArticle>>> {
        return repository.getTopHeadlines(countryCode, category)
    }

    suspend fun saveAllNews(articles: PagingData<NewsArticle>) {
        return repository.saveAllNews(articles)
    }
}