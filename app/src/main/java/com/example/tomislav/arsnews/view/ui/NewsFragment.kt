package com.example.tomislav.arsnews.view.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.utils.NetworkUtils
import com.example.tomislav.arsnews.utils.UiUtils
import com.example.tomislav.arsnews.view.adapter.LatestNewsAdapter
import com.example.tomislav.arsnews.viewmodel.NewsViewModel
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.news_fragment.*
import javax.inject.Inject

class NewsFragment():DaggerFragment(){

    lateinit var model: NewsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var subscriptions: CompositeDisposable= CompositeDisposable()
    lateinit var waitForNetwork:Disposable



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.news_fragment, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this,viewModelFactory).get(NewsViewModel::class.java)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        initLatestAdapter()
        initSwipeToRefresh()
        loadNewsFromNewtwork()
    }
    private fun initLatestAdapter() {
        val glide = Glide.with(this)
        val adapter = LatestNewsAdapter(glide,model)
        news_list?.layoutManager = LinearLayoutManager(activity)
        news_list?.adapter = adapter

    }

    private fun waitForConnection(): Disposable {
        waitForNetwork = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ connected ->
                    if (connected!!) {
                        UiUtils.showToast(context!!,"Connected",Toast.LENGTH_SHORT)
                        model.updateNews()
                        subscriptions.remove(waitForNetwork)
                    }

                }
                ) { throwable -> UiUtils.handleThrowable(throwable) }
        return waitForNetwork
    }


    private fun loadNewsFromNewtwork(){
        if (!NetworkUtils.isNetworkAvailable(context!!)) {
            Log.d("Connection error: ", "No connection!")
            UiUtils.showToast(context!!, "Offline mode", Snackbar.LENGTH_LONG)
            subscriptions.add(waitForConnection())
        }
        else{
            model.updateNews()
        }
    }


    private fun initSwipeToRefresh() {
        swipe_refresh.setOnRefreshListener {
            if (!NetworkUtils.isNetworkAvailable(context!!)) {
                UiUtils.showToast(context!!, "No connection!",Toast.LENGTH_SHORT)
                swipe_refresh.isRefreshing=false
            }
            else{
                model.updateNews()
                swipe_refresh.isRefreshing=false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}