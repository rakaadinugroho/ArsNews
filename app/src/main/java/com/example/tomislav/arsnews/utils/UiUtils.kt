package com.example.tomislav.arsnews.utils

import android.content.Context
import android.util.Log
import android.widget.Toast


object UiUtils {

    fun handleThrowable(throwable: Throwable) {
        Log.e("RxError: ", "There was an error!", throwable)
    }

    fun showToast(context: Context, message: String, length: Int) {
        Toast.makeText(context,message,length).show()
    }
}