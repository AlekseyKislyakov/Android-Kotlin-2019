package com.example.lesson_11_kislyakov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ONE_COLUMN = 1
    private val THREE_COLUMN = 3
    private val NINE_COLUMN = 9

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customDiagramView.setData(NINE_COLUMN)
        customDiagramView.setOnClickListener {
            customDiagramView.showAnimation()
        }
    }
}
