package com.example.lesson_5_kislyakov.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lesson_5_kislyakov.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_3.*

class Activity3 : AppCompatActivity() {

    val ACTIVITY_REQUEST_CODE: Int = 101
    val STRING_OBJECT_TITLE = "LOL"
    val TAG = "myTag"

    companion object {
        fun createInstance(context: Context): Intent {
            val intent = Intent(context, Activity3::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_3)

        toolbarActivity3.setNavigationOnClickListener {
            super.onBackPressed()
        }

        buttonGoto1From3.setOnClickListener {
            startActivity(Activity1.createInstance(this))
        }

        buttonGoto5From3.setOnClickListener {
            startActivityForResult(Activity5.createInstance(this), ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ACTIVITY_REQUEST_CODE)
            if(resultCode == Activity.RESULT_OK)
                if (data?.hasExtra(STRING_OBJECT_TITLE)!!)
                    Snackbar.make(window.decorView.rootView, data.getStringExtra(STRING_OBJECT_TITLE), Snackbar.LENGTH_SHORT)
                        .show()
    }
}
