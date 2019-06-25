package com.example.lesson_6_kislyakov


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_basic.view.*

private const val EXTRA_MESSAGE = "param1"
private const val IMAGE_MESSAGE = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FragmentBasic : Fragment() {

    companion object {
        fun newInstance(message: String, img: String): FragmentBasic {
            val fragment = FragmentBasic()
            val bdl = Bundle()
            bdl.putString(EXTRA_MESSAGE, message)
            bdl.putString(IMAGE_MESSAGE, img)
            fragment.arguments = bdl
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_basic, container, false)

        view.textViewFragmentBase.text = arguments?.getString(EXTRA_MESSAGE)

        Glide.with(context)
            .load(arguments?.getString(IMAGE_MESSAGE))
            .into(view.imageViewFragmentBase)
        return view
    }


}
