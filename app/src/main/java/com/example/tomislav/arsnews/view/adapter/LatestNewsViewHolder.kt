package com.example.tomislav.arsnews.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.utils.UiUtils
import kotlinx.android.synthetic.main.latest_news_list_item.view.*


class LatestNewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val title: TextView = itemView.latest_news_item_title
    private val description = itemView.latest_news_item_description
    private val time = itemView.latest_news_item_time
    private val author = itemView.latest_news_item_author
    private var newsItem : NewsItem? = null
    /*init {
        view.setOnClickListener {
            post?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }*/

    fun bind(newsItem: NewsItem?) {
        this.newsItem = newsItem
        title.text = newsItem?.title?: "Loading..."
        description.text = newsItem?.description?: "Loading..."
       val elapsedTime = UiUtils.getElapsedTimeString(newsItem?.publishedAt)
        time.text = elapsedTime?: "Loading..."
        author.text = newsItem?.source?: "Loading..."
    }



    companion object {
        fun create(parent: ViewGroup): LatestNewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.latest_news_list_item, parent, false)
            return LatestNewsViewHolder(view)
        }
    }



}