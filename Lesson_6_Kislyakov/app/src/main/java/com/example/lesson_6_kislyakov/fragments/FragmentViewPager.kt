package com.example.lesson_6_kislyakov.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.lesson_6_kislyakov.utils.DotsIndicator
import com.example.lesson_6_kislyakov.R
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentViewPager : Fragment() {

    val PAGE_COUNT = 3

    companion object {
        fun newInstance(): FragmentViewPager {
            return FragmentViewPager()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val viewPagerAdapter = MyAdapter(
            childFragmentManager,
            PAGE_COUNT
        )
        view.viewPagerBanner.adapter = viewPagerAdapter

        //create dots indicator
        val dotsIndicator =
            DotsIndicator(viewPagerAdapter.count, view)
        dotsIndicator.addDots()

        //set up on scroll dots update
        view.viewPagerBanner.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // change active dots
                dotsIndicator.updateDots(position)
            }

            override fun onPageSelected(position: Int) {
            }

        })

        return view
    }

    class MyAdapter internal constructor(fm: FragmentManager, pageCount: Int) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        val adapterSize = pageCount
        val PAGE_IMAGE_1 = 0
        val PAGE_IMAGE_2 = 1
        val PAGE_IMAGE_3 = 2

        override fun getCount(): Int {
            return adapterSize
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                PAGE_IMAGE_1 -> FragmentBasic.newInstance(
                    "Картинка 1",
                    "https://pp.userapi.com/c854224/v854224218/851da/kveZXKf8sZQ.jpg"
                )
                PAGE_IMAGE_2 -> FragmentBasic.newInstance(
                    "Картинка 2",
                    "https://www.researchgate.net/profile/Wu_Jeng_Li/publication/258744201/figure/fig1/AS:392591104331781@1470612421920/AndroidManifestxml-file-of-the-Android-controller-application.png"
                )
                else -> FragmentBasic.newInstance(
                    "Картинка 3",
                    "https://cdn-images-1.medium.com/max/1600/1*5zMb0f02mchKOQ1N5RYYDw.png"
                )
            }
        }
    }


}



