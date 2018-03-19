package com.example.tomislav.arsnews.data.repository



import com.example.tomislav.arsnews.data.model.NewsAPIModels
import com.example.tomislav.arsnews.data.model.NewsItem
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.util.concurrent.Executor
import javax.inject.Singleton


@Singleton
class NewsRepository(private val newsAPIService: NewsAPIService, private val networkExecutor: Executor,private val realmDatabase: Realm){

    companion object {
        const val PAGE_SIZE = 5
        const val INITIAL_LOAD_SIZE = 5
        const val PREFETCH_DISTANCE = 10
    }
    init {
        updateNews()
    }

    fun getLatestNews():RealmResults<NewsItem> =
        realmDatabase.where(NewsItem::class.java)
                .equalTo("category","latest")
                .sort("publishedAt",Sort.DESCENDING)
                .findAllAsync()



    fun getTopNews():RealmResults<NewsItem> =
        realmDatabase.where(NewsItem::class.java)
                .equalTo("category","top")
                .sort("publishedAt",Sort.DESCENDING)
                .findAllAsync()

    fun updateNews(){
        async(UI) {
            val data: Deferred<List<NewsItem>> = bg {
                // Runs in background
                fetchTopHeadlines()

            }

            // This code is executed on the UI thread
            val x = data.await()
            realmDatabase.executeTransaction{
                realmDatabase.insert(x)
            }

        }
        async(UI) {
            val data: Deferred<List<NewsItem>> = bg {
                // Runs in background
                fetchLatestNews()

            }

            // This code is executed on the UI thread
            val x = data.await()
            realmDatabase.executeTransaction{
                realmDatabase.insert(x)

            }
        }
        async(UI){
            realmDatabase.executeTransaction{
                realmDatabase.insert(NewsItem().apply {
                    title="News"
                    description="descriptis"
                    source="JA"
                    publishedAt="2018-03-1T16:32:05Z"
                    category="latest"
                })
            }
        }


    }

    private fun fetchLatestNews():List<NewsItem>
            = newsAPIService.getNewsForCountry().execute().body()?.articles?.map { it -> transformNewsItem(it,"latest") } as List<NewsItem>



    private fun fetchTopHeadlines():List<NewsItem> = newsAPIService.getNewsForSource().execute().body()?.articles?.map { it -> transformNewsItem(it,"top") } as List<NewsItem>




    private fun transformNewsItem(newsItemAPI: NewsAPIModels.NewsItemAPI, topOrLatest:String):NewsItem{
        return NewsItem().apply {
            source=newsItemAPI.source?.name!!
            title=newsItemAPI.title
            description=newsItemAPI.description
            url=newsItemAPI.url
            urlToImage=newsItemAPI.urlToImage
            publishedAt=newsItemAPI.publishedAt
            category=topOrLatest
        }


    }
    //TODO realm close
}