package com.example.lesson_5_kislyakov.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.lesson_5_kislyakov.R
import com.example.lesson_5_kislyakov.models.Row
import com.example.lesson_5_kislyakov.adapters.RowsAdapter
import com.example.lesson_5_kislyakov.adapters.ViewPagerAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_6.*



class Activity6 : AppCompatActivity() {

    companion object {
        fun createInstance(context: Context): Intent {
            return Intent(context, Activity6::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_6)

        // in Activity's onCreate() for instance

        Glide.with(this)
                .load("https://img4.goodfon.ru/wallpaper/nbig/5/83/dom-pole-sumerki.jpg")
                .apply(RequestOptions()
                        .optionalCenterCrop())
                .into(imageViewToolbarBackground)

        //set listeners
        constraintLayoutServices.setOnClickListener {
            Toast.makeText(this, textViewServicesAll.text, Toast.LENGTH_SHORT).show()
        }

        textViewAddService.setOnClickListener {
            Toast.makeText(this, textViewAddService.text, Toast.LENGTH_SHORT).show()
        }

        //create element array for recyclerview
        val elements: ArrayList<Row> = ArrayList()
        elements.add(Row("Царь пышка", "Скидка 10% на выпечку по коду", "Лермонтовский пр, 52, МО №1",
                "https://themoscowcity.com/local/templates/moscow_city/assets/images/bg/moscow-product.png"))
        elements.add(Row("Химчистка «Май?»", "Скидка 5% на химчистку пальто", "Лермонтовский пр, 48",
                "https://cdn.icon-icons.com/icons2/387/PNG/256/Boxy-Calculons-Evil-Half-Brother-icon-256_37836.png"))
        elements.add(Row("Шаверма У Ашота", "При покупке шавермы, фалафель бесплатно", "Лермонтовский пр, 52, МО №1",
                "https://cdn.icon-icons.com/icons2/640/PNG/512/o-brand-single-letter-square_icon-icons.com_59168.png"))

        //set up adapter and recyclerview
        val adapterRows = RowsAdapter(elements)
        adapterRows.onItemClick = { item ->
            Snackbar.make(window.decorView.rootView, item.header, Snackbar.LENGTH_SHORT).show()
        }
        recyclerViewServices.layoutManager = LinearLayoutManager(this)
        recyclerViewServices.adapter = adapterRows

        //set up viewpager
        val adapterViewPager = ViewPagerAdapter(this)
        viewPager.adapter = adapterViewPager

        //below is implementation of page indicator
        val dotscount = adapterViewPager.count
        val dots = arrayOfNulls<ImageView>(dotscount)

        // add dots for indicator
        for (i in 0 until dotscount) {

            dots[i] = ImageView(applicationContext)
            dots[i]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nonactive_dot))

            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            params.setMargins(8, 0, 8, 0)

            linearLayoutSliderDots.addView(dots[i], params)

        }
        //set first as active
        //then on swipe change state of the dot
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                for (j in 0 until dotscount) {
                    dots[j]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.nonactive_dot))
                }
                dots[position]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.active_dot))
            }

            override fun onPageSelected(position: Int) {

            }

        })

    }
}
