package com.example.lesson_5_kislyakov

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_1.*
import kotlinx.android.synthetic.main.activity_3.*

class Activity3 : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context): Intent {
            val intent = Intent(context, Activity3::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        toolbarActivity3.setNavigationOnClickListener {
            super.onBackPressed()
        }

        buttonGoto1From3.setOnClickListener {
            startActivity(Activity1.createInstance(this))
        }
    }
}
