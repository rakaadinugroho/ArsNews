package com.example.tomislav.arsnews.view.ui

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
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
import com.example.tomislav.arsnews.utils.waitForConnection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import android.util.TypedValue
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import io.reactivex.Flowable
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


class MainActivity : DaggerAppCompatActivity(){

    lateinit var model: NewsViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private val BACK_STACK_ROOT_TAG = "root_fragment"
    var subscriptions = CompositeDisposable()
    lateinit var waitForNetwork:Disposable
    private var searchState = false
    private val fragment=NewsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(my_toolbar)
        model = ViewModelProviders.of(this,viewModelFactory).get(NewsViewModel::class.java)


        if (!NetworkUtils.isNetworkAvailable(this)) {
            setNoConnectionFragment()
            waitForConnection()
            Log.d("Connection error: ", "No connection!")
        }
        else{
            addFragment(fragment,R.id.fragment_container)

        }



        search_editext.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(NetworkUtils.isNetworkAvailable(this@MainActivity)){
                        if(!search_editext.text.toString().isEmpty()){
                            searchState=true
                            fragment.performSearch(search_editext.text.toString())
                        }
                        else
                            Snackbar.make(fragment_container,"Enter text for search",Snackbar.LENGTH_LONG).show()
                    }
                    else
                        Snackbar.make(fragment_container,"No connection",Snackbar.LENGTH_LONG).show()

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(search_editext.windowToken, 0)
                    return true;
                }
                return false;
            }
        })

        search_editext.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s?.isNullOrEmpty()!!)
                    search_icon.setImageDrawable(getDrawable(R.drawable.ic_search))
                else
                    search_icon.setImageDrawable(getDrawable(R.drawable.abc_ic_clear_material))

            }

        })

        search_icon.setOnClickListener({search_editext.text.clear()})
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
        replaceFragment(fragment,R.id.fragment_container)
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }


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
        if (searchState){
            fragment.backToNews()
            searchState=false
        }
        else
            finish()
    }



}
