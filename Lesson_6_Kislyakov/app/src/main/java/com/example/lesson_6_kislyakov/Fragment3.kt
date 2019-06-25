package com.example.lesson_6_kislyakov


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_3.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_3, container, false)
        // create on/off flag for the button (show/hide banner)
        var bannerShown = false
        view.buttonShowBanner.setOnClickListener {
            val viewPagerFragment = FragmentViewPager()
            //
            if (!bannerShown) {
                childFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutBannerContainer,
                    viewPagerFragment
                ).commit()
                bannerShown = true
                view.buttonShowBanner.text = "СВЕРНУТЬ БАННЕР"
            } else {
                childFragmentManager.beginTransaction()
                    .remove(childFragmentManager.findFragmentById(R.id.frameLayoutBannerContainer)!!)
                    .commit()
                bannerShown = false
                view.buttonShowBanner.text = "ПОКАЗАТЬ БАННЕР"


            }
        }
        return view
    }


}
