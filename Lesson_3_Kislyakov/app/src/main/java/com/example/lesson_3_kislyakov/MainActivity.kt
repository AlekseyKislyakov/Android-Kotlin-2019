package com.example.lesson_3_kislyakov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_relative.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setting up menu for toolbar
        toolbar.inflateMenu(R.menu.menu_main)

        //setting up on back click listener
        toolbar.setNavigationOnClickListener{
            Toast.makeText(this, "BACK", Toast.LENGTH_SHORT).show()
        }

        //setting up on menu(pencil) click listener
        toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.action_pencil){
                Toast.makeText(this, "Pencil", Toast.LENGTH_SHORT).show()
            }
            true
        }

        //setting up onclicklisteners imageviews(edit region and logout)
        imageViewChangeRegion.setOnClickListener{
            Toast.makeText(this, "Change region button", Toast.LENGTH_SHORT).show()
        }
        imageViewLogout.setOnClickListener{
            Toast.makeText(this, "Logout button", Toast.LENGTH_SHORT).show()
        }

        //setting up user fields
        textViewProfile.text = getString(R.string.profile_example)
        textViewName.text = getString(R.string.name_example)
        textViewSurname.text = getString(R.string.surname_example)
        textViewEmail.text = getString(R.string.email_example)
        textViewLogin.text = getString(R.string.login_example)
        textViewRegion.text = getString(R.string.region_example)


    }
}
