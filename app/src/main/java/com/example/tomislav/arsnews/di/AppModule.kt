package com.example.tomislav.arsnews.di

import com.example.tomislav.arsnews.data.repository.NewsAPIService
import com.example.tomislav.arsnews.data.repository.NewsRepository
import com.example.tomislav.arsnews.utils.rx.RxSchedulers
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule{
    @Provides
    @Singleton
    fun provideCurrencyRepository(newsAPIService: NewsAPIService, rxSchedulers: RxSchedulers): NewsRepository = NewsRepository(newsAPIService,rxSchedulers)

}