package com.example.lesson_5_kislyakov.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson_5_kislyakov.models.Data
import com.example.lesson_5_kislyakov.R
import kotlinx.android.synthetic.main.activity_5.*

class Activity5 : AppCompatActivity() {

    val STRING_OBJECT_TITLE = "LOL"
    val DATA_OBJECT_KEY = "myData"

    var data: Data = Data("")

    companion object {
        fun createInstance(context: Context): Intent {
            val intent = Intent(context, Activity5::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_5)
        Log.d("myTag", "ONCREATE")

        if(savedInstanceState != null){
            val temp: Data? = savedInstanceState.getParcelable<Data>(DATA_OBJECT_KEY)
            if(temp != null){
                data = temp
                textViewSaveData.text = data.value
            }
        } else {
            Log.d("myTag", "BUNDLE IS NULL")
        }

        toolbarActivity5.setNavigationOnClickListener {
            super.onBackPressed()
        }

        buttonDeliverResult.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(STRING_OBJECT_TITLE, editTextDeliverResult.text.toString())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        buttonSaveData.setOnClickListener {
            data.value = editTextDeliverResult.text.toString()
            textViewSaveData.text = data.value
        }
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        if(data.value != ""){
            savedInstanceState.putParcelable(DATA_OBJECT_KEY, data)
        }

    }
}
