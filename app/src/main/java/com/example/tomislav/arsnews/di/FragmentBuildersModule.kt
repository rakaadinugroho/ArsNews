package com.example.tomislav.arsnews.di

import com.example.tomislav.arsnews.view.ui.NewsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeNewsFragmentFragment(): NewsFragment

}