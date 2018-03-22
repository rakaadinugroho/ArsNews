package com.example.tomislav.arsnews.viewmodel

import android.arch.lifecycle.ViewModel
import com.example.tomislav.arsnews.data.repository.NewsRepository
import javax.inject.Inject

class NewsViewModel @Inject constructor(private val repository: NewsRepository):ViewModel(){

    suspend fun getLatestNews() = repository.getLatestNews()

    suspend fun getTopHeadLines() = repository.getTopNews()

    suspend fun getSearchNews(query:String, page:Int, pageSize:Int )
            = repository.getNewsForSearch(query,page,pageSize)
}