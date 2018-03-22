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
import com.example.tomislav.arsnews.utils.loadImg
import kotlinx.android.synthetic.main.news_list_item.view.*
import kotlinx.android.synthetic.main.top_news_list_item.view.*

class TopNewsItemDelegateAdapter(val viewActions: OnViewSelectedListener) : ViewTypeDelegateAdapter {



    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TopNewsItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as TopNewsItemViewHolder
        holder.bind(item as NewsItem)
    }

    inner class TopNewsItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.top_news_list_item)) {

        private val title: TextView = itemView.top_news_item_title
        private val time = itemView.top_news_item_time
        private val author = itemView.top_news_item_source
        private val image = itemView.top_news_item_image

        fun bind(item: NewsItem) {
            title.text = item.title
            time.text = item.publishedAt.getElapsedTimeString().dropLast(3)
            author.text = item.source.name
            image.loadImg(item.urlToImage)

            super.itemView.setOnClickListener { viewActions.onItemSelected(item.url)}
        }

    }
}