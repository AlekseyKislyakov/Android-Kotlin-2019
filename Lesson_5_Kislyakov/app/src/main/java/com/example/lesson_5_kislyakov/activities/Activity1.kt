package com.example.lesson_5_kislyakov.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson_5_kislyakov.R
import kotlinx.android.synthetic.main.activity_1.*
import java.lang.Long

class Activity1 : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context): Intent {
            val intent = Intent(context, Activity1::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        toolbarActivity1.setNavigationOnClickListener {
            super.onBackPressed()
        }

        buttonGoto4From1.setOnClickListener {
            startActivity(Activity4.createInstance(this, System.currentTimeMillis().toString()))
        }

        buttonGoto2From1.setOnClickListener {
            startActivity(Activity2.createInstance(this))
        }

        buttonGoto6From1.setOnClickListener {
            startActivity(Activity6.createInstance(this))
        }
    }
}
