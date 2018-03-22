package com.example.tomislav.arsnews.data.repository




import com.example.tomislav.arsnews.data.model.NewsItem
import com.example.tomislav.arsnews.data.model.NewsItemTop
import com.example.tomislav.arsnews.data.model.NewsResponse
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult
import javax.inject.Singleton


@Singleton
class NewsRepository(private val newsAPIService: NewsAPIService){


    suspend fun getLatestNews():List<NewsItem>{
        val result = newsAPIService.getNewsForCountry().awaitResult()

        return when (result) {
            is Result.Ok -> process(result.value)
            is Result.Error -> throw Throwable("HTTP error: ${result.response.message()}")
            is Result.Exception -> throw result.exception
            else -> {
                throw Throwable("Something went wrong, please try again later.")
            }
        }
    }

    suspend fun getTopNews():List<NewsItemTop>{
        val result = newsAPIService.getNewsForSource().awaitResult()

        return when (result) {
            is Result.Ok -> processTop(result.value)
            is Result.Error -> throw Throwable("HTTP error: ${result.response.message()}")
            is Result.Exception -> throw result.exception
            else -> {
                throw Throwable("Something went wrong, please try again later.")
            }
        }
    }

    suspend fun getNewsForSearch(query:String, page:Int, pageSize:Int):List<NewsItem>{
        val result = newsAPIService.getNewsForSearch(query,pageSize,page).awaitResult()

        return when (result) {
            is Result.Ok -> process(result.value)
            is Result.Error -> throw Throwable("HTTP error: ${result.response.message()}")
            is Result.Exception -> throw result.exception
            else -> {
                throw Throwable("Something went wrong, please try again later.")
            }
        }
    }

    private fun process(response: NewsResponse): List<NewsItem> {
        return response.articles

    }

    private fun processTop(response: NewsResponse): List<NewsItemTop> {
        return response.articles.map {
            val item = it
            NewsItemTop(item)
        }

    }

}