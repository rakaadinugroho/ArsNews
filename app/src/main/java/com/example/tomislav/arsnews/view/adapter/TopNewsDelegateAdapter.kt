package com.example.tomislav.arsnews.view.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.utils.OnViewSelectedListener
import com.example.tomislav.arsnews.utils.adapter.ViewType
import com.example.tomislav.arsnews.utils.adapter.ViewTypeDelegateAdapter
import com.example.tomislav.arsnews.utils.inflate
import kotlinx.android.synthetic.main.top_news_recycler_view_item.view.*

class TopNewsDelegateAdapter(val viewActions: OnViewSelectedListener) : ViewTypeDelegateAdapter {


    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TopNewsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as TopNewsViewHolder
        holder.bind()
    }

    inner class TopNewsViewHolder(val parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.top_news_recycler_view_item)) {

        val recyclerView = itemView.top_new_recycler_view
        val topHeadlinesTitle = itemView.top_headlines_title
        val latestNewsTitle = itemView.latest_news_title
        val topNewsAdapter  = TopNewsAdapter(viewActions)

        fun bind() {
            var layoutManager = LinearLayoutManager(parent.context)
            layoutManager.orientation= LinearLayoutManager.HORIZONTAL
            recyclerView.apply {
                setHasFixedSize(true)
                setLayoutManager(layoutManager)
                adapter=topNewsAdapter
            }
            topHeadlinesTitle.text="Top Headlines"
            latestNewsTitle.text= "Latest News"

        }
    }
}