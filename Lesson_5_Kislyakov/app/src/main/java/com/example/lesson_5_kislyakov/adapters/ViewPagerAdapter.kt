package com.example.lesson_5_kislyakov.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.lesson_5_kislyakov.R

class ViewPagerAdapter(private val context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    val arrayUris = arrayOf("http://www.theatreglas.ru/uploads/2016/02/Kreml.jpg",
            "https://cdn-st1.rtr-vesti.ru/p/xw_1411909.jpg",
            "https://www.svoiludi.ru/images/tb/203/paris-1240083454-ib_w687h357.jpg")

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return arrayUris.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.viewpager_layout, null)
        val image = v.findViewById<View>(R.id.imageViewViewPager) as ImageView

        Glide.with(container)
                .load(arrayUris[position])
                .into(image)

        image.setOnClickListener {
            Toast.makeText(context, "Viewpager $position", Toast.LENGTH_SHORT).show()
        }

        val vp = container as ViewPager
        vp.addView(v, 0)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }
}