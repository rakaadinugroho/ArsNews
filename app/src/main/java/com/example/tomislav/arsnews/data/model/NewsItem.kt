package com.example.tomislav.arsnews.data.model

import com.example.tomislav.arsnews.utils.adapter.AdapterConstants
import com.example.tomislav.arsnews.utils.adapter.ViewType

open class NewsItem(
        val source: Source,
         val title:String,
         val description:String,
         val url:String,
         val urlToImage:String,
         val publishedAt:String):ViewType{

    constructor(item:NewsItem) : this(item.source,item.title,item.description,item.url,item.urlToImage,item.publishedAt)

    override fun getViewType() = AdapterConstants.NEWS


}
