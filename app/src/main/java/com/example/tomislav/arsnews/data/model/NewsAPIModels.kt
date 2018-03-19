package com.example.tomislav.arsnews.data.model


import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


object NewsAPIModels{
    data class NewsResponse(val status:String,val totalResults:Int,val articles:List<NewsItemAPI>)

    data class NewsItemAPI(val source: Source?,
                           val author:String,
                            val title:String,
                           val description:String,
                           val url:String,
                           val urlToImage:String,
                           val publishedAt:String,
                           var category:String)

    data class NewsResponseSearch(val articles:List<NewsItemSearch>)
    data class NewsItemSearch(val source:Source,
                              val author:String,
                              val title:String,
                              val description:String,
                              val url:String,
                              val urlToImage:String,
                              val publishedAt:String)

    data class Source(val name:String)




}