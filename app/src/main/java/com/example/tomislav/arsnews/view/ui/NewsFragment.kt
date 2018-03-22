package com.example.tomislav.arsnews.view.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
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
import com.example.tomislav.arsnews.utils.*
import com.example.tomislav.arsnews.utils.adapter.InfiniteScrollListener
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
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*


class NewsFragment():DaggerFragment(), OnViewSelectedListener {

    lateinit var model: NewsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var subscriptions: CompositeDisposable= CompositeDisposable()
    lateinit var waitForNetwork:Disposable
    private lateinit var newsAdapter:NewsAdapter
    protected var job: Job? = null
    private var query=""
    private var waitingForNetwork  = false

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
        getLatestAndTopNews(false)

    }

    private fun initLatestAdapter() {
        val animation = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_fall_down)
        news_list.layoutAnimation=animation
        news_list?.layoutManager = LinearLayoutManager(activity)
        newsAdapter=NewsAdapter(this)
        news_list?.adapter = newsAdapter

    }

    private fun waitForConnection(): Disposable {
        waitForNetwork = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ connected ->
                    if (connected!!) {
                        UiUtils.showToast(context!!,"Connected - Auto refresh",Toast.LENGTH_LONG)
                        getLatestAndTopNews(true)
                        subscriptions.remove(waitForNetwork)
                        waitingForNetwork=false
                    }

                }
                ) { throwable -> UiUtils.handleThrowable(throwable) }
        return waitForNetwork
    }


    fun performSearch(queryString: String){
        query=queryString
        newsAdapter.showSearch(true)
        news_list.addOnScrollListener(InfiniteScrollListener(this::getNewsForSearch,news_list.layoutManager as LinearLayoutManager))
        getNewsForSearch(1,10)
        swipe_refresh.isEnabled=false
    }

    fun backToNews(){
        swipe_refresh.isEnabled=true
        newsAdapter.showSearch(false)
        news_list.clearOnScrollListeners()
    }

    private fun getLatestAndTopNews(refresh:Boolean){

        job= launch(UI) {
            try {
                val latest = model.getLatestNews()
                val top = model.getTopHeadLines()
                if(refresh){
                    newsAdapter.apply {
                        reload()
                        addNews(latest)
                        topNewsViewHolder.topNewsAdapter.addNews(top)
                    }
                }
                else{
                    newsAdapter.apply {
                        addNews(latest)
                        topNewsViewHolder.topNewsAdapter.addNews(top)
                    }
                }
                waitingForNetwork=false

            } catch (e: Throwable) {
                if (isVisible) {
                    Snackbar.make(news_list, e.message.orEmpty(), Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY") { getLatestAndTopNews(false) }
                            .show()
                }
            }
        }
    }

    private fun getNewsForSearch(page:Int,pageSize:Int){
        if(page == 1)
            newsAdapter.setLoading()
        job= launch(UI) {
            try {
                val searchNews = model.getSearchNews(query,1,10)
                newsAdapter.apply {
                    addNews(searchNews)
                }
            } catch (e: Throwable) {
                if (isVisible) {
                    Snackbar.make(news_list, e.message.orEmpty(), Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY") { getLatestAndTopNews(false) }
                            .show()
                }
            }
        }
    }

    private fun initSwipeToRefresh() {
        swipe_refresh.setColorSchemeColors(ContextCompat.getColor(context!!,R.color.colorPrimary),ContextCompat.getColor(context!!,R.color.colorAccent))
        swipe_refresh.setOnRefreshListener {
            if (!NetworkUtils.isNetworkAvailable(context!!)) {
                Snackbar.make(news_list,"No connection",Snackbar.LENGTH_LONG).show()
                if(!waitingForNetwork){
                    subscriptions.add(waitForConnection())
                    waitingForNetwork=true
                }

                swipe_refresh.isRefreshing=false
            }
            else{
                if(!waitingForNetwork) {
                    getLatestAndTopNews(true)
                    waitingForNetwork=true
                }
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