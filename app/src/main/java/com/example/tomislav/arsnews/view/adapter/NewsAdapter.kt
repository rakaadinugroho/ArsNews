package com.example.tomislav.arsnews.view.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.utils.OnViewSelectedListener
import com.example.tomislav.arsnews.utils.adapter.AdapterConstants
import com.example.tomislav.arsnews.utils.adapter.ViewType
import com.example.tomislav.arsnews.utils.adapter.ViewTypeDelegateAdapter
import java.util.ArrayList

class NewsAdapter(listener: OnViewSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }
    private val topNewsItem = object : ViewType {
        override fun getViewType() = AdapterConstants.TOP_NEWS
    }
    lateinit var topNewsViewHolder:TopNewsDelegateAdapter.TopNewsViewHolder

    init {
        delegateAdapters.apply {
            put(AdapterConstants.LOADING, LoadingDelegateAdapter())
            put(AdapterConstants.NEWS, NewsDelegateAdapter(listener))
            put(AdapterConstants.TOP_NEWS, TopNewsDelegateAdapter(listener))
        }
        items = ArrayList()
        items.add(topNewsItem)
        items.add(loadingItem)

    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == AdapterConstants.TOP_NEWS)
            topNewsViewHolder=holder as TopNewsDelegateAdapter.TopNewsViewHolder
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun addNews(news: List<NewsItem>) {
        // first remove loading and notify
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        // insert news and the loading at the end of the list
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeChanged(initPosition, items.size + 1 /* plus loading item */)
    }

    fun clearAndAddNews(news: List<NewsItem>) {
        items.clear()
        notifyItemRangeRemoved(0, getLastPosition())
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeInserted(0, items.size)
    }

    fun getNews(): List<NewsItem> =
            items.filter { it.getViewType() == AdapterConstants.NEWS }.map { it as NewsItem }

    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex
}