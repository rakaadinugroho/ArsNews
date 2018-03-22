package com.example.tomislav.arsnews.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.utils.OnViewSelectedListener
import com.example.tomislav.arsnews.utils.adapter.ViewType
import com.example.tomislav.arsnews.utils.adapter.ViewTypeDelegateAdapter
import com.example.tomislav.arsnews.utils.getElapsedTimeString
import com.example.tomislav.arsnews.utils.inflate
import kotlinx.android.synthetic.main.news_list_item.view.*

class NewsDelegateAdapter(val viewActions: OnViewSelectedListener) : ViewTypeDelegateAdapter {



    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return NewsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as NewsViewHolder
        holder.bind(item as NewsItem)
    }

    inner class NewsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.news_list_item)) {

        private val title: TextView = itemView.latest_news_item_title
        private val description = itemView.latest_news_item_description
        private val time = itemView.latest_news_item_time
        private val author = itemView.latest_news_item_author

        fun bind(item: NewsItem) {
            title.text = item?.title
            description.text = item.description
            time.text = item.publishedAt.getElapsedTimeString()
            author.text = item.source?.name

            super.itemView.setOnClickListener { viewActions.onItemSelected(item.url)}
        }
    }
}