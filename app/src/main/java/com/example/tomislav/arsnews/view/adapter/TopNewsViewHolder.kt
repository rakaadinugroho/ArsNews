package com.example.tomislav.arsnews.view.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.data.model.NewsItem
import io.realm.RealmResults
import kotlinx.android.synthetic.main.top_new_recycler_view_item.view.*

class TopNewsViewHolder(view:View, private val glide: RequestManager): RecyclerView.ViewHolder(view){
    val recyclerView = itemView.top_new_recycler_view
    val topHeadlinesTitle = itemView.top_headlines_title
    val latestNewsTitle = itemView.latest_news_title

    fun bind(topNewsList:RealmResults<NewsItem>, context: Context) {
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation=LinearLayoutManager.HORIZONTAL
        recyclerView.apply {
            setHasFixedSize(true)
            setLayoutManager(layoutManager)
            adapter=TopNewsAdapter(topNewsList,glide)
        }
        topHeadlinesTitle.text="Top Headlines"
        latestNewsTitle.text= "Latest News"
    }



    companion object {
        fun create(parent: ViewGroup, glide: RequestManager): TopNewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.top_new_recycler_view_item, parent, false)
            return TopNewsViewHolder(view,glide)
        }
    }
}