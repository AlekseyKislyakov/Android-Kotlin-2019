package com.example.lesson_5_kislyakov.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lesson_5_kislyakov.R
import kotlinx.android.synthetic.main.activity_2.*

class Activity2 : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context): Intent {
            val intent = Intent(context, Activity2::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)

        toolbarActivity2.setNavigationOnClickListener {
            super.onBackPressed()
        }

        buttonGoto3From2.setOnClickListener {
            startActivity(Activity3.createInstance(this))
        }

    }
}
