package com.example.tomislav.arsnews.di

import com.example.tomislav.arsnews.App
import com.example.tomislav.arsnews.viewmodel.NewsViewModel
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class),
                        (AppModule::class),
                        (NetworkModule::class),
                        (ActivityBuilder::class),
                        (ViewModelModule::class)])

interface AppComponent: AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()

    fun inject(newsViewModel: NewsViewModel)
}