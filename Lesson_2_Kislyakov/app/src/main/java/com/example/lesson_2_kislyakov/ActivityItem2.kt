package com.example.lesson_2_kislyakov

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityItem2 : AppCompatActivity() {

    companion object {
        fun createStartIntent(context: Context): Intent {
            val intent = Intent(context, ActivityItem2::class.java)
            intent.setAction("com.example.lesson_2_kislyakov")
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item2)
    }
}
