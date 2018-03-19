package com.example.tomislav.arsnews.di

import com.example.tomislav.arsnews.data.repository.NewsAPIService
import com.example.tomislav.arsnews.data.repository.NewsRepository
import dagger.Module
import dagger.Provides
import io.realm.Realm
import java.util.concurrent.Executor
import javax.inject.Singleton

@Module
class AppModule{
    @Provides
    @Singleton
    fun provideCurrencyRepository(newsAPIService: NewsAPIService, executor: Executor, realmDatabase:Realm): NewsRepository = NewsRepository(newsAPIService, executor,realmDatabase)

    @Provides
    @Singleton
    fun provideRealmDatabase():Realm = Realm.getDefaultInstance()
}