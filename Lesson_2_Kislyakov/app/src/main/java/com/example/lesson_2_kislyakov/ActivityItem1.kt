package com.example.lesson_2_kislyakov

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityItem1 : AppCompatActivity() {

    companion object {
        fun createStartIntent(context: Context): Intent {
            val intent = Intent(context, ActivityItem1::class.java)
            intent.setAction("com.example.lesson_2_kislyakov")
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item1)
    }
}
