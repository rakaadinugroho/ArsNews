package com.example.tomislav.arsnews.view.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.utils.*
import com.example.tomislav.arsnews.view.adapter.NewsAdapter
import com.example.tomislav.arsnews.viewmodel.NewsViewModel
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.news_fragment.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

import javax.inject.Inject
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


class NewsFragment():DaggerFragment(), OnViewSelectedListener {

    lateinit var model: NewsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var subscriptions: CompositeDisposable= CompositeDisposable()
    lateinit var waitForNetwork:Disposable
    private val newsAdapter by androidLazy { NewsAdapter(this) }
    protected var job: Job? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.news_fragment)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this,viewModelFactory).get(NewsViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLatestAdapter()
        initSwipeToRefresh()
        loadNewsFromNetwork()
    }

    private fun initLatestAdapter() {
        news_list?.layoutManager = LinearLayoutManager(activity)
        news_list?.adapter = newsAdapter

    }

    private fun waitForConnection(): Disposable {
        waitForNetwork = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ connected ->
                    if (connected!!) {
                        UiUtils.showToast(context!!,"Connected",Toast.LENGTH_SHORT)
                        //model.updateNews()
                        subscriptions.remove(waitForNetwork)
                    }

                }
                ) { throwable -> UiUtils.handleThrowable(throwable) }
        return waitForNetwork
    }


    private fun loadNewsFromNetwork(){
        if (!NetworkUtils.isNetworkAvailable(context!!)) {
            Log.d("Connection error: ", "No connection!")
            UiUtils.showToast(context!!, "Offline mode", Snackbar.LENGTH_LONG)
            subscriptions.add(waitForConnection())
        }
        else{
            getLatestAndTopNews()
        }
    }
    //TODO implemnt calls
    private fun getLatestAndTopNews(){

        job= launch(UI) {
            try {
                val latest = model.getLatestNews()
                val top = model.getTopHeadLines()
                newsAdapter.apply {
                    addNews(latest)
                    topNewsViewHolder.topNewsAdapter.addNews(top)
                }
            } catch (e: Throwable) {
                if (isVisible) {
                    Log.d("TAGIC",e.message.orEmpty(),e)
                    Snackbar.make(news_list, e.message.orEmpty(), Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY") { getLatestAndTopNews() }
                            .show()
                }
            }
        }




    }

    private fun initSwipeToRefresh() {
        swipe_refresh.setColorSchemeColors(ContextCompat.getColor(context!!,R.color.colorPrimary),ContextCompat.getColor(context!!,R.color.colorAccent))
        swipe_refresh.setOnRefreshListener {
            if (!NetworkUtils.isNetworkAvailable(context!!)) {
                UiUtils.showToast(context!!, "No connection!",Toast.LENGTH_SHORT)
                swipe_refresh.isRefreshing=false
            }
            else{
                //getLatestAndTopNews()
                swipe_refresh.isRefreshing=false
            }
        }

    }

    override fun onResume() {
        super.onResume()
        job = null
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
        job = null
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

    override fun onItemSelected(url: String?) {
        if (url.isNullOrEmpty()) {
            Snackbar.make(news_list, "No URL assigned to this news", Snackbar.LENGTH_LONG).show()
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}