package com.example.tomislav.arsnews.data.model

import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey


open class NewsItem(): RealmObject(){
    var source: String? = null
    @PrimaryKey var title:String? = null
    var description:String? = null
    var url:String? = null
    var urlToImage:String? = null
    @Index var publishedAt:String? = null
    @Index var category:String? = null

}
