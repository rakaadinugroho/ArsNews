package com.example.tomislav.arsnews.data.repository

import com.example.tomislav.arsnews.utils.rx.RxSchedulers
import javax.inject.Singleton


@Singleton
class NewsRepository(private val newsAPIService: NewsAPIService, private val rxSchedulers: RxSchedulers){

}