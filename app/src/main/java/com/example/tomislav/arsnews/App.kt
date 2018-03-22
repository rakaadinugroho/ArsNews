package com.example.tomislav.arsnews

import com.crashlytics.android.Crashlytics
import com.example.tomislav.arsnews.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid

class App: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out App> {
        Fabric.with(this,Crashlytics())
        JodaTimeAndroid.init(this)
        return DaggerAppComponent.builder().create(this)
    }
}