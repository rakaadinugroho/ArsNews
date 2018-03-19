package com.example.tomislav.arsnews.view.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.example.tomislav.arsnews.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private val BACK_STACK_ROOT_TAG = "root_fragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            addFragment(NewsFragment(),R.id.fragment_container)
        }
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
        supportFragmentManager.inTransaction{replace(frameId, fragment).addToBackStack(null)}

    }
}
