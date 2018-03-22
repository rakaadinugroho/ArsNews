package com.example.tomislav.arsnews.data.model

import com.example.tomislav.arsnews.utils.adapter.AdapterConstants

class NewsItemTop(newsItem:NewsItem):NewsItem(newsItem){

    override fun getViewType(): Int {
        return AdapterConstants.TOP_NEWS_ITEM
    }
}