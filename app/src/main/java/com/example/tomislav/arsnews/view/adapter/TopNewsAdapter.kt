package com.example.tomislav.arsnews.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.example.tomislav.arsnews.data.model.NewsItem
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmResults

class TopNewsAdapter(private val topNewsList:RealmResults<NewsItem>, private val glide: RequestManager):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    init {
        topNewsList.addChangeListener(object : OrderedRealmCollectionChangeListener<RealmResults<NewsItem>> {
            override fun onChange(t: RealmResults<NewsItem>?, changeSet: OrderedCollectionChangeSet?) {
                this@TopNewsAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TopNewsItemViewHolder.create(parent,glide)
    }

    override fun getItemCount() = topNewsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TopNewsItemViewHolder).bind(topNewsList.get(position))
    }

}