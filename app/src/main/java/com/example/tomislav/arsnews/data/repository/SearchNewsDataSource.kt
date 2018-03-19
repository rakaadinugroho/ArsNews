package com.example.tomislav.arsnews.data.repository
import android.arch.paging.PositionalDataSource
import com.example.tomislav.arsnews.data.model.NewsAPIModels
import java.util.concurrent.Executor

class SearchNewsDataSource(private val newsAPIService: NewsAPIService,
                           private val retryExecutor: Executor): PositionalDataSource<NewsAPIModels.NewsItemSearch>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<NewsAPIModels.NewsItemSearch>) {

    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<NewsAPIModels.NewsItemSearch>) {
    }


}