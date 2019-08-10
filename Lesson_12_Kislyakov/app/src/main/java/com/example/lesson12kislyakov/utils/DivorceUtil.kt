package com.example.lesson12kislyakov.utils

import android.util.Log
import com.bumptech.glide.Glide
import com.example.lesson12kislyakov.R
import com.example.lesson12kislyakov.data.models.Divorce

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class DivorceUtil {

    val STATE_OPEN = 1
    val STATE_NEAR = 0
    val STATE_CLOSED = -1

    private val MILLS_IN_HOUR: Long = 3600000

    private val INITIAL_YEAR = 1970
    private val INITIAL_MONTH = 0
    private val INITIAL_DAY = 1

    private val ST_PETERSBURG_TIMEZONE = "GMT+3"


    // converts divorce list into one string
    fun divorceListToString(divorceList: List<Divorce>): String{
        val formatter = SimpleDateFormat("h:mm")

        var resultString = StringBuilder()
        for (divorce in divorceList){
            resultString.append(formatter.format(divorce.start))
                .append(" - ")
                .append(formatter.format(divorce.end))
                .append("   ")
        }
        return resultString.toString()
    }

    fun divorceState(divorcesList: List<Divorce>): Int {

        var state = STATE_OPEN

        val cal = Calendar.getInstance(TimeZone.getTimeZone(ST_PETERSBURG_TIMEZONE))
        cal.set(INITIAL_YEAR, INITIAL_MONTH, INITIAL_DAY)
        val now = cal.time
        Log.d("MyTag", cal.toString())

        for (divorce in divorcesList) {

            if (divorce.end.after(now) && divorce.start.time - now.time < MILLS_IN_HOUR && state != STATE_CLOSED) {
                state = STATE_NEAR // желтый мост
            }

            if (divorce.end.after(now) && now.after(divorce.start)) {
                state = STATE_CLOSED // красный мост
            }
        }
        return state
    }

    fun setStatus(divorcesList: List<Divorce>): Int{
        when (divorceState(divorcesList)) {
            STATE_OPEN -> {
                return R.drawable.ic_brige_normal
            }
            STATE_NEAR -> {
                return R.drawable.ic_brige_soon
            }
            STATE_CLOSED -> {
                return R.drawable.ic_brige_late
            }
        }
    return 0
    }
}