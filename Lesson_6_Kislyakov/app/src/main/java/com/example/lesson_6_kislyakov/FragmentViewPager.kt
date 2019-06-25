package com.example.lesson_6_kislyakov


import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentViewPager : Fragment() {

    val MARGIN_DIP = 4 // margin for the indicator dots

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val viewPagerAdapter = MyAdapter(childFragmentManager, getFragments())
        view.viewPagerBanner.adapter = viewPagerAdapter

        val dotscount = viewPagerAdapter.count
        val dots = arrayOfNulls<ImageView>(dotscount)

        // add dots for indicator
        for (i in 0 until dotscount) {

            dots[i] = ImageView(context)

            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.nonactive_dot
                )
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


            params.setMargins(MARGIN_DIP.px, 0, MARGIN_DIP.px, 0)

            view.linearLayoutSliderDots.addView(dots[i], params)

        }
        //set first as active
        //then on swipe change state of the dot
        dots[0]?.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.active_dot))

        view.viewPagerBanner.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                for (j in 0 until dotscount) {
                    dots[j]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            context!!,
                            R.drawable.nonactive_dot
                        )
                    )
                }
                dots[position]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context!!,
                        R.drawable.active_dot
                    )
                )


            }

            override fun onPageSelected(position: Int) {

            }

        })

        return view
    }

    fun getFragments(): ArrayList<Fragment> {
        val fragmentList = ArrayList<Fragment>()

        fragmentList.add(
            FragmentBasic.newInstance(
                "Картинка 1",
                "https://www.android-examples.com/wp-content/uploads/2015/12/androidmanifest.png"
            )
        )
        fragmentList.add(
            FragmentBasic.newInstance(
                "Картинка 2",
                "https://www.researchgate.net/profile/Wu_Jeng_Li/publication/258744201/figure/fig1/AS:392591104331781@1470612421920/AndroidManifestxml-file-of-the-Android-controller-application.png"
            )
        )
        fragmentList.add(
            FragmentBasic.newInstance(
                "Картинка 3",
                "https://cdn-images-1.medium.com/max/1600/1*5zMb0f02mchKOQ1N5RYYDw.png"
            )
        )

        return fragmentList
    }

    class MyAdapter internal constructor(fm: FragmentManager, fragments: ArrayList<Fragment>) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val adapterFragments = fragments

        override fun getCount(): Int {
            return adapterFragments.size
        }

        override fun getItem(position: Int): Fragment {
            return adapterFragments[position]
        }
    }

    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

}



