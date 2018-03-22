package com.example.tomislav.arsnews.utils.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

class InfiniteScrollListener(
        private val fetchMoreNews: (page:Int, pageSize:Int) -> Unit,
        val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold = 5
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pageCount = 0
    private val PAGE_SIZE = 10

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {
            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                Log.i("InfiniteScrollListener", "End reached")
                fetchMoreNews(pageCount++,PAGE_SIZE)
                loading = true
            }
        }
    }

}