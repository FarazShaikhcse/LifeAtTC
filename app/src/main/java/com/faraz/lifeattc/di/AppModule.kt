package com.faraz.lifeattc.di

import com.faraz.lifeattc.data.remote.WebsiteService
import com.faraz.lifeattc.data.repository.WebsiteRepository
import com.faraz.lifeattc.data.repository.WebsiteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.truecaller.com/") // Base URL
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWebsiteService(retrofit: Retrofit): WebsiteService {
        return retrofit.create(WebsiteService::class.java)
    }

    @Provides
    @Singleton
    fun provideWebsiteRepository(websiteService: WebsiteService): WebsiteRepository {
        return WebsiteRepositoryImpl(websiteService)
    }
}