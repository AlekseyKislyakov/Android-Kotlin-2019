package com.example.lesson_6_kislyakov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson_6_kislyakov.fragments.Fragment1
import com.example.lesson_6_kislyakov.fragments.Fragment2
import com.example.lesson_6_kislyakov.fragments.Fragment3
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutContainer,
            Fragment1.newInstance()
        ).commit()

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_navigation_item1 -> supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutContainer,
                    Fragment1.newInstance()
                ).commit()

                R.id.bottom_navigation_item2 -> supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutContainer,
                    Fragment2.newInstance()
                ).commit()

                R.id.bottom_navigation_item3 -> supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutContainer,
                    Fragment3.newInstance()
                ).commit()
            }

            true
        }
    }

}
