package com.example.tomislav.arsnews.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.tomislav.arsnews.viewmodel.NewsViewModel
import com.example.tomislav.arsnews.viewmodel.NewsViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey( NewsViewModel::class )
    abstract fun bindNewsViewModel( currencyViewModel: NewsViewModel ): ViewModel

    @Binds
    abstract fun bindViewModelFactory( currencyFactory: NewsViewModelFactory): ViewModelProvider.Factory

}