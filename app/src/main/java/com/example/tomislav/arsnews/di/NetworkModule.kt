package com.example.tomislav.arsnews.di

import com.example.tomislav.arsnews.data.repository.NewsAPIService
import com.example.tomislav.arsnews.utils.rx.AppRxSchedulers
import com.example.tomislav.arsnews.utils.rx.RxSchedulers
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule{

    companion object {
        val BASE_URL = "https://newsapi.org/v2/"
    }

    @Singleton
    @Provides
    internal fun provideApiService(client: OkHttpClient, moshi: MoshiConverterFactory): NewsAPIService {
        val retrofit = Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(moshi)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return  retrofit.create(NewsAPIService::class.java)
    }


    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        return builder.build()
    }


    @Provides
    fun provideMoshiClient(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    fun provideRxSchedulers(): RxSchedulers = AppRxSchedulers()


}