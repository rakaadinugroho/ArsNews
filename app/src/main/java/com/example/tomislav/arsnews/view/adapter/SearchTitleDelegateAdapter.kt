package com.example.tomislav.arsnews.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.utils.adapter.ViewType
import com.example.tomislav.arsnews.utils.adapter.ViewTypeDelegateAdapter
import com.example.tomislav.arsnews.utils.inflate

class SearchTitleDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.search_title_item))
}