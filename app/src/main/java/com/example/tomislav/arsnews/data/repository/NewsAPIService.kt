package com.example.tomislav.arsnews.data.repository

import com.example.tomislav.arsnews.BuildConfig
import com.example.tomislav.arsnews.data.model.NewsAPIModels
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {
    @GET("top-headlines")
    fun getNewsForSource(@Query("sources")source:String = "bbc-news",
                         @Query("pageSize")pageSize:Int = 10,
                         @Query("page") page:Int = 1,
                        @Query("apikey")apiKey:String=BuildConfig.NewsAPIKey): Call<NewsAPIModels.NewsResponse>

    @GET("top-headlines")
    fun getNewsForCountry(@Query("country")country:String = "us",
                         @Query("pageSize")pageSize:Int = 20,
                         @Query("page") page:Int = 1,
                         @Query("apikey")apiKey:String=BuildConfig.NewsAPIKey): Call<NewsAPIModels.NewsResponse>

    @GET("everything")
    fun getNewsForSearch(@Query("q")query: String,
                        @Query("pageSize")pageSize:Int = 10,
                        @Query("page") page:Int = 1,
                        @Query("apikey")apiKey:String=BuildConfig.NewsAPIKey): Call<NewsAPIModels.NewsResponseSearch>
}