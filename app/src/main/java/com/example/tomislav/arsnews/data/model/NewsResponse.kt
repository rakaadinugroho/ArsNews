package com.example.tomislav.arsnews.data.model

data class NewsResponse(val status:String,
                        val totalResults:Int,
                        val articles:List<NewsItem>)
