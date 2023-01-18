package com.lord_ukaka.worldnews.injector

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lord_ukaka.worldnews.core.utils.Constants.BASE_URL
import com.lord_ukaka.worldnews.core.utils.Constants.DATABASE_NAME
import com.lord_ukaka.worldnews.core.utils.navigator.Navigator
import com.lord_ukaka.worldnews.core.utils.navigator.NavigatorImpl
import com.lord_ukaka.worldnews.data.local.NewsDatabase
import com.lord_ukaka.worldnews.data.remote.NewsApi
import com.lord_ukaka.worldnews.data.repository.NewsRepositoryImpl
import com.lord_ukaka.worldnews.domain.repository.NewsRepository
import com.lord_ukaka.worldnews.domain.usecases.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  AppModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(app: Application): NewsDatabase {
        return Room.databaseBuilder(
            app,
            NewsDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(NewsApi::class.java)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideNewsRepository(
        db: NewsDatabase,
        api: NewsApi
    ): NewsRepository {
        return NewsRepositoryImpl(db, api)
    }

    @Singleton
    @Provides
    fun provideValidations(): GetValidations {
        return GetValidations(
            validateEmail = ValidateEmail(),
            validatePassword = ValidatePassword(),
            validatePhoneNumber = ValidatePhoneNumber(),
            validateRepeatedPassword = ValidateRepeatedPassword(),
            validateTerms = ValidateTerms()
        )
    }
}