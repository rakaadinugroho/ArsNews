package com.example.tomislav.arsnews.di

import com.example.tomislav.arsnews.data.repository.NewsAPIService
import com.example.tomislav.arsnews.data.repository.NewsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule{
    @Provides
    @Singleton
    fun provideCurrencyRepository(newsAPIService: NewsAPIService): NewsRepository = NewsRepository(newsAPIService)

}