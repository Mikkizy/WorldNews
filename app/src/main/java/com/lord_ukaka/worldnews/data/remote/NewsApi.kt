package com.lord_ukaka.worldnews.data.remote

import com.lord_ukaka.worldnews.BuildConfig
import com.lord_ukaka.worldnews.data.remote.dto.Article
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        countryCode: String = "ng",
        @Query("page")
        pageNumber: Int = 1,
        @Query("category")
        category: String = "general",
        @Query("apikey")
        apiKey: String = BuildConfig.API_KEY
    ): List<Article>

    @GET("v2/everything")
    suspend fun SearchForNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey: String = BuildConfig.API_KEY
    ): List<Article>
}