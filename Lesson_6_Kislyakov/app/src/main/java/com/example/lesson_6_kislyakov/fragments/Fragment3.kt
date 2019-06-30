package com.example.lesson_6_kislyakov.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lesson_6_kislyakov.R
import kotlinx.android.synthetic.main.fragment_3.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment3 : Fragment() {

    companion object {
        fun newInstance(): Fragment3 {
            return Fragment3()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_3, container, false)
        // create on/off flag for the button (show/hide banner)
        var bannerShown = false
        view.buttonShowBanner.setOnClickListener {
            //
            if (!bannerShown) {
                childFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutBannerContainer,
                    FragmentViewPager.newInstance()
                ).commit()
                bannerShown = true
                view.buttonShowBanner.text = getString(R.string.hide_banner)
            } else {
                childFragmentManager.beginTransaction()
                    .remove(childFragmentManager.findFragmentById(R.id.frameLayoutBannerContainer)!!)
                    .commit()
                bannerShown = false
                view.buttonShowBanner.text = getString(R.string.show_banner)


            }
        }
        return view
    }
}
