package com.example.tomislav.arsnews.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.utils.UiUtils
import kotlinx.android.synthetic.main.top_news_list_item.view.*

class TopNewsItemViewHolder(view:View,private val glide: RequestManager ):RecyclerView.ViewHolder(view){
    private val title: TextView = itemView.top_news_item_title
    private val image = itemView.top_news_item_image
    private val time = itemView.top_news_item_time
    private val source = itemView.top_news_item_source
    /*init {
        view.setOnClickListener {
            post?.url?.let { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                view.context.startActivity(intent)
            }
        }
    }*/

    fun bind(newsItem: NewsItem?) {
        title.text = newsItem?.title?: "Loading..."
        source.text = newsItem?.source?: "Loading..."
        val elapsedTime = UiUtils.getElapsedTimeString(newsItem?.publishedAt)
        time.text = elapsedTime?: "Loading..."
        //TODO solve picture crop and scale
        glide.load(newsItem?.urlToImage).into(image)
    }



    companion object {
        fun create(parent: ViewGroup, glide: RequestManager): TopNewsItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.top_news_list_item, parent, false)
            return TopNewsItemViewHolder(view,glide)
        }
    }
}