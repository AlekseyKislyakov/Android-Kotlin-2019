package com.example.lesson_2_kislyakov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //start first exercise activity
        buttonStartItem1.setOnClickListener{
            startActivity(ActivityItem1.createStartIntent(this))
        }

        //start second exercise activity
        buttonStartItem2.setOnClickListener{
            startActivity(ActivityItem2.createStartIntent(this))
        }

    }

}
