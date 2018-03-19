package com.example.tomislav.arsnews.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.format.ISODateTimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

object UiUtils {

    fun handleThrowable(throwable: Throwable) {
        Log.e("RxError: ", "There was an error!", throwable)
    }

    fun showToast(context: Context, message: String, length: Int) {
        Toast.makeText(context,message,length).show()
    }



    fun getElapsedTimeString(dateTime:String?):String?{
        var startTime= DateTime()
        try {
            startTime = ISODateTimeFormat.dateTimeParser().parseDateTime(dateTime)
        }
        catch (exception: ParseException){
            Log.e("Time parse error: ", exception.message)
            return null
        }
        val endTime = DateTime()
        var elapsedTime:String = ""
        var difference = endTime.millis.minus(startTime.millis)

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays = difference / daysInMilli
        if(elapsedDays == 0.toLong()){

            difference %= daysInMilli
            val elapsedHours = difference / hoursInMilli
            if (elapsedHours == 0.toLong()){

                difference %= hoursInMilli
                val elapsedMinutes = difference / minutesInMilli
                if (elapsedMinutes.equals(0.toLong()).or(elapsedMinutes == 1.toLong()) ){
                    elapsedTime= "A minute ago by "
                }
                else{
                    elapsedTime= "${elapsedMinutes.toString()} minutes ago by "
                }

            }
            else{
                when(elapsedHours){
                    1.toLong() -> elapsedTime= "${elapsedHours.toString()} hour ago by "
                    else -> elapsedTime= "${elapsedHours.toString()} hours ago by "
                }
            }

        }
        else{
            when(elapsedDays){
                1.toLong() -> elapsedTime= "${elapsedDays.toString()} day ago by "
                else -> elapsedTime= "${elapsedDays.toString()} days ago by "
            }
        }

        return elapsedTime
    }

}