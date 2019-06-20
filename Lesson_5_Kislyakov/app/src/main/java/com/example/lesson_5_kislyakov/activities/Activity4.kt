package com.example.lesson_5_kislyakov.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson_5_kislyakov.R
import kotlinx.android.synthetic.main.activity_4.*
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*

class Activity4 : AppCompatActivity() {

    companion object {
        val EXTRA_TITLE: String = "CURRENT_TIME"

        fun createInstance(context: Context, title: String): Intent {
            val intent = Intent(context, Activity4::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            return intent
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if(intent?.hasExtra(EXTRA_TITLE)!!){
            textViewShowTime.text = TimeConverter(intent.getStringExtra(EXTRA_TITLE))
        } else {
            textViewShowTime.text = getString(R.string.error_message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_4)

        toolbarActivity4.setNavigationOnClickListener {
            super.onBackPressed()
        }

        if(intent.hasExtra(EXTRA_TITLE)){
            textViewShowTime.text = TimeConverter(intent.getStringExtra(EXTRA_TITLE))
        } else {
            textViewShowTime.text = getString(R.string.error_message)
        }

        buttonGoto4.setOnClickListener {
            startActivity(createInstance(this, Long.toString(System.currentTimeMillis())))
        }
    }

    private fun TimeConverter(mills: String): String {
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = java.lang.Long.parseLong(mills)
        return formatter.format(calendar.time)
    }
}
