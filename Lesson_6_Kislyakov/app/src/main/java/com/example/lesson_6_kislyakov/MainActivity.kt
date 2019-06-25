package com.example.lesson_6_kislyakov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment1 = Fragment1()
        val fragment2 = Fragment2()
        val fragment3 = Fragment3()
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayoutContainer,
            fragment1
        ).commit()

        //bottomNavigationView.inflateMenu(R.menu.menu_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            var selectedFragment: Fragment? = null
            when (it.itemId) {
                R.id.bottom_navigation_item1 -> selectedFragment = fragment1
                R.id.bottom_navigation_item2 -> selectedFragment = fragment2
                R.id.bottom_navigation_item3 -> selectedFragment = fragment3
            }
            if(selectedFragment != null){
                supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutContainer,
                    selectedFragment
                ).commit()
            }
            true
        }
    }

}
