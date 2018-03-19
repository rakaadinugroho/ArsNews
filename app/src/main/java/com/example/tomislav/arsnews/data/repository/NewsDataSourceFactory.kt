package com.example.tomislav.arsnews.data.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.example.tomislav.arsnews.data.model.NewsAPIModels
import java.util.concurrent.Executor

class NewsDataSourceFactory(
        private val newsAPIService: NewsAPIService,
        private val retryExecutor: Executor) : DataSource.Factory<String, NewsAPIModels.NewsItemSearch> {
    val sourceLiveData = MutableLiveData<SearchNewsDataSource>()

    override fun create(): DataSource<String, NewsAPIModels.NewsItemSearch> {
        val source = SearchNewsDataSource(newsAPIService, retryExecutor)
        sourceLiveData.postValue(source)
        return source as DataSource<String, NewsAPIModels.NewsItemSearch>
    }
}