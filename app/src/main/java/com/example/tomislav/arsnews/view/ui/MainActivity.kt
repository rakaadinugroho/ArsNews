package com.example.tomislav.arsnews.view.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.example.tomislav.arsnews.R
import com.example.tomislav.arsnews.viewmodel.NewsViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import android.widget.RelativeLayout
import android.view.ViewGroup.MarginLayoutParams
import com.example.tomislav.arsnews.utils.NetworkUtils
import com.example.tomislav.arsnews.utils.UiUtils
import com.example.tomislav.arsnews.utils.androidLazy
import com.example.tomislav.arsnews.utils.waitForConnection
import com.example.tomislav.arsnews.view.adapter.NewsAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import android.util.TypedValue




class MainActivity : DaggerAppCompatActivity(){

    lateinit var model: NewsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val BACK_STACK_ROOT_TAG = "root_fragment"
    var subscriptions = CompositeDisposable()
    lateinit var waitForNetwork:Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
        model = ViewModelProviders.of(this,viewModelFactory).get(NewsViewModel::class.java)

  //      if (savedInstanceState == null) {
    //        addFragment(NewsFragment(),R.id.fragment_container)
      //  }

        if (!NetworkUtils.isNetworkAvailable(this)) {
            setNoConnectionFragment()
            waitForConnection()
            Log.d("Connection error: ", "No connection!")
        }
        else{
            addFragment(NewsFragment(),R.id.fragment_container)

        }
    }

    private fun setNoConnectionFragment(){
        supportActionBar?.hide()
       val marginParams = MarginLayoutParams(fragment_container.layoutParams)
       marginParams.setMargins(0, 0, 0, 0)
       val layoutParams = RelativeLayout.LayoutParams(marginParams)
       fragment_container.layoutParams=layoutParams
        addFragment(OfflineFragment(),R.id.fragment_container)
    }

     fun swapToNewsFragment(){
         val r = this.getResources()
         val px = TypedValue.applyDimension(
                 TypedValue.COMPLEX_UNIT_DIP,
                 60F,
                 r.getDisplayMetrics()
         ).toInt()
        supportActionBar?.show()
        val marginParams = MarginLayoutParams(fragment_container.layoutParams)
        marginParams.setMargins(0, px, 0, 0)
        val layoutParams = RelativeLayout.LayoutParams(marginParams)
        fragment_container.layoutParams=layoutParams
        replaceFragment(NewsFragment(),R.id.fragment_container)
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    /*fun MainActivity.show(hero: Models.Hero) {
        val bundle=Bundle()
        bundle.putParcelable("hero",hero)
        var fragment = HeroesFragment()
        fragment.arguments = bundle
        replaceFragment(fragment,R.id.fragment_container)
    }*/

    fun DaggerAppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
        supportFragmentManager.apply {
            popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            inTransaction { add(frameId, fragment).addToBackStack(BACK_STACK_ROOT_TAG) }
        }
    }

    fun DaggerAppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction{ replace(frameId, fragment).addToBackStack(null)}
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }

    override fun onBackPressed() {
        finish()
    }


}
