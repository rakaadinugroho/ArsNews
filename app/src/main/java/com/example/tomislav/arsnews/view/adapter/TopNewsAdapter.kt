package com.example.tomislav.arsnews.view.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.data.model.NewsItemTop
import com.example.tomislav.arsnews.utils.OnViewSelectedListener
import com.example.tomislav.arsnews.utils.adapter.AdapterConstants
import com.example.tomislav.arsnews.utils.adapter.ViewType
import com.example.tomislav.arsnews.utils.adapter.ViewTypeDelegateAdapter
import java.util.ArrayList

class TopNewsAdapter(listener: OnViewSelectedListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object : ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.TOP_NEWS_ITEM, TopNewsItemDelegateAdapter(listener))
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        items = ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])



    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun addNews(news: List<NewsItemTop>) {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        items.clear()
        items.addAll(news)
        notifyItemRangeChanged(initPosition, items.size )
    }
}