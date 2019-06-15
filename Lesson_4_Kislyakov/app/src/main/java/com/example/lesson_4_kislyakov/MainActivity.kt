package com.example.lesson_4_kislyakov

import android.os.Bundle
import android.widget.GridLayout.HORIZONTAL
import android.widget.GridLayout.VERTICAL
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //this is necessary to programmatically set vector drawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        setContentView(R.layout.activity_main)

        //set up toolbar
        toolbar.inflateMenu(R.menu.menu_main)

        //setting up on back(home & info) click listeners
        toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "BACK", Toast.LENGTH_SHORT).show()
        }

        //setting up on menu(home & info) click listeners
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.itemToolbarHome) {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            } else if (it.itemId == R.id.itemToolbarInfo) {
                AlertDialog.Builder(this)
                        .setTitle("Info")
                        .setMessage("Hello")
                        .setPositiveButton("hi", null)
                        .create()
                        .show()
            }
            true
        }

        //create adapter with 10 items
        val adapter: TileAdapter = TileAdapter(10, this)

        //set onClickListener to items in recyclerview
        //call is in the adapter function
        adapter.onItemClick = { contact ->
            Snackbar.make(window.decorView.rootView, contact.header, Snackbar.LENGTH_SHORT).show()
        }

        //set up layout manager with 2 columns
        val layoutManager = GridLayoutManager(this, 2)

        //set up sizes for the items. 2 is for guard and base items because the are to be twice as width
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    adapter.DESCRIPTION_RED -> 1
                    adapter.DEFAULT -> 1
                    adapter.ONEINTWO -> 2
                    adapter.BASECELL -> 2
                    else -> -1
                }
            }
        }

        //add decorations
        recyclerViewTile.addItemDecoration(ItemDecorationAlbumColumns(
                resources.getDimensionPixelSize(R.dimen.photos_list_spacing),
            2))

        //set up recyclerview
        recyclerViewTile.layoutManager = layoutManager
        recyclerViewTile.adapter = adapter
    }
}
