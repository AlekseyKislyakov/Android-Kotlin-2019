package com.example.lesson_5_kislyakov

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent



class Activity1 : AppCompatActivity() {

    val EXTRA_TITLE: String = "LOL"

    fun createInstance(context: Context, title: String): Intent {
        val intent = Intent(context, Activity1::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
