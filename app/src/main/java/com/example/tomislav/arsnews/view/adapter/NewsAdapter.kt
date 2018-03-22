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
import android.view.animation.AnimationUtils
import com.example.tomislav.arsnews.R


class NewsAdapter(private val listener: OnViewSelectedListener,private val mRecyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>
    lateinit var tempItems: ArrayList<ViewType>
    private var searchShowState=false
    var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

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
        setLoading()

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
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)

        items.addAll(news)
        notifyItemRangeChanged(initPosition, items.size + 1 )
    }

    fun reload(){
        items.clear()
        items = ArrayList()
        items.add(topNewsItem)
        setLoading()
        runLayoutAnimation()
    }

    fun showSearch(showState:Boolean){

        if(showState){
            if(searchShowState)
                items.clear()
            else {
                searchShowState=true
                tempItems = items.clone() as ArrayList<ViewType>
                items.clear()
            }
            //TODO add search title delegate
        }
        else{
            items=tempItems.clone() as ArrayList<ViewType>
            tempItems.clear()
            searchShowState=false
        }

        runLayoutAnimation()
    }

    private fun runLayoutAnimation() {
        val context = mRecyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)

        mRecyclerView.layoutAnimation = controller
        mRecyclerView.adapter.notifyDataSetChanged()
        mRecyclerView.scheduleLayoutAnimation()
    }

    fun setLoading(){
        items.add(loadingItem)
    }

}