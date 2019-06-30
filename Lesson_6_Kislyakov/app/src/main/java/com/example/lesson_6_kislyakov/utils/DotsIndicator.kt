package com.example.lesson_6_kislyakov.utils

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.example.lesson_6_kislyakov.R
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class DotsIndicator(val dotsCount: Int, val view: View) {
    val dots = arrayOfNulls<ImageView>(dotsCount)
    fun addDots() {
        for (i in 0 until dotsCount) {

            dots[i] = ImageView(view.context)

            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    view.context!!,
                    R.drawable.nonactive_dot
                )
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.setMargins(
                view.resources.getDimension(R.dimen.dots_indicator_padding).toInt(),
                0, view.resources.getDimension(R.dimen.dots_indicator_padding).toInt(), 0
            )

            view.linearLayoutSliderDots.addView(dots[i], params)

        }
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(view.context!!,
            R.drawable.active_dot
        ))
    }

    fun updateDots(positionActive: Int) {
        for (j in 0 until dotsCount) {
            dots[j]?.setImageDrawable(
                ContextCompat.getDrawable(
                    view.context!!,
                    R.drawable.nonactive_dot
                )
            )
        }
        dots[positionActive]?.setImageDrawable(
            ContextCompat.getDrawable(
                view.context!!, R.drawable.active_dot
            )
        )
    }
}