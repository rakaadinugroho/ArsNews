package com.example.tomislav.arsnews.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.tomislav.arsnews.data.repository.NewsRepository
import javax.inject.Inject

class NewsViewModel @Inject constructor(val repository: NewsRepository):ViewModel(){

    fun getLatestNews() = repository.getLatestNews()

    fun getTopHeadLines() = repository.getTopNews()

    fun updateNews() = repository.updateNews()
}