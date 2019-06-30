package com.example.lesson_6_kislyakov.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lesson_6_kislyakov.R
import kotlinx.android.synthetic.main.fragment_1.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment1 : Fragment() {

    companion object {
        fun newInstance(): Fragment1 {
            return Fragment1()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        val toolbar = view.toolbarFragment1
        toolbar.inflateMenu(R.menu.menu_toolbar_item1)

        toolbar.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.itemSearch -> Toast.makeText(context, getString(
                    R.string.item_toolbar_search
                ), Toast.LENGTH_SHORT).show()
                it.itemId == R.id.itemSubitem1 -> Toast.makeText(context, "Subitem 1", Toast.LENGTH_SHORT).show()
                it.itemId == R.id.itemSubitem2 -> Toast.makeText(context, "Subitem 2", Toast.LENGTH_SHORT).show()
                it.itemId == R.id.itemSubitem3 -> Toast.makeText(context, "Subitem 3", Toast.LENGTH_SHORT).show()
            }
            true
        }
        // Inflate the layout for this fragment
        return view
    }



}
