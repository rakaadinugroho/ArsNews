package com.example.tomislav.arsnews.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.viewmodel.NewsViewModel
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmResults


class LatestNewsAdapter(private val glide: RequestManager,
                        viewModel: NewsViewModel) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val TOP_NEWS_LAYOUT = R.layout.top_new_recycler_view_item
        const val LATEST_NEWS_ITEM = R.layout.latest_news_list_item
    }

    var latestList: RealmResults<NewsItem>
    var topList:RealmResults<NewsItem>
    lateinit var context: Context

    init {
        latestList = viewModel.getLatestNews()
        topList = viewModel.getTopHeadLines()
        latestList.addChangeListener(object : OrderedRealmCollectionChangeListener<RealmResults<NewsItem>>{
            override fun onChange(t: RealmResults<NewsItem>?, changeSet: OrderedCollectionChangeSet?) {
                this@LatestNewsAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context=parent.context
        return when(viewType){
            TOP_NEWS_LAYOUT -> TopNewsViewHolder.create(parent,glide)
            else -> LatestNewsViewHolder.create(parent)
        }

    }

    override fun getItemCount(): Int = latestList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            TOP_NEWS_LAYOUT -> {
                (holder as TopNewsViewHolder).bind(topList,context)
            }
            else -> {
                (holder as LatestNewsViewHolder).bind(latestList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> TOP_NEWS_LAYOUT
            else -> LATEST_NEWS_ITEM
        }
    }



    /* companion object {
         val NEWS_COMPARATOR = object :DiffUtil.ItemCallback<NewsAPIModels.NewsItem>(){
             override fun areContentsTheSame(oldItem: NewsAPIModels.NewsItem, newItem: NewsAPIModels.NewsItem): Boolean =
                     oldItem == newItem

             override fun areItemsTheSame(oldItem: NewsAPIModels.NewsItem, newItem: NewsAPIModels.NewsItem): Boolean =
                     oldItem.title == newItem.title

         }

     }*/

}