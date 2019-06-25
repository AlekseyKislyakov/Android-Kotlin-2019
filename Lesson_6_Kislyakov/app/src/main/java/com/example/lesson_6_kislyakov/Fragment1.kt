package com.example.lesson_6_kislyakov


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_1.*


/**
 * A simple [Fragment] subclass.
 *
 */
class Fragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarFragment1)
        toolbar.inflateMenu(R.menu.menu_toolbar_item1)

        toolbar.setOnMenuItemClickListener {
            when {
                it.itemId == R.id.itemSearch -> Toast.makeText(activity, "Search", Toast.LENGTH_SHORT).show()
                it.itemId == R.id.itemSubitem1 -> Toast.makeText(activity, "Subitem 1", Toast.LENGTH_SHORT).show()
                it.itemId == R.id.itemSubitem2 -> Toast.makeText(activity, "Subitem 2", Toast.LENGTH_SHORT).show()
                it.itemId == R.id.itemSubitem3 -> Toast.makeText(activity, "Subitem 3", Toast.LENGTH_SHORT).show()
            }
            true
        }
        // Inflate the layout for this fragment
        return view
    }



}
