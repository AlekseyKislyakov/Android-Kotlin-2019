package com.example.lesson_1_kislyakov

import android.os.Bundle
import android.view.inputmethod.EditorInfo

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //create hashmap
        val hashMap = HashMap<Long, Student>()

        //set on enter listener
        input_string_edittext.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //split given string
                val parts = input_string_edittext.text.toString().split(" ")
                //check if wrong number of parameters given
                if (parts.size != 4) {
                    Toast.makeText(this, "Wrong input data!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    //if ok create put student object and id to hashmap
                    hashMap.put(
                        System.currentTimeMillis(),
                        Student(parts[0], parts[1], parts[2], parts[3])
                    )
                    //clear text for the next student
                    input_string_edittext.text.clear()
                    Toast.makeText(this, "Item added", Toast.LENGTH_SHORT)
                        .show()

                }
                true
            } else {
                false
            }
        }

        //show data on button click
        view_string_button.setOnClickListener {
            var str = StringBuilder()
            for ((key, value) in hashMap) {
                str.append("$key ${value.present()}")
            }
            if (str.toString() == "")
                showstringlist_textview.text = "Nothing to show"
            else
                showstringlist_textview.text = str.toString()
        }


    }

    //class Student, idk add id here or not
    // TODO change birthday year to int and do exceptions
    class Student(val name: String, val surname: String, val grade: String, val birthdayYear: String) {
        //function to output the data
        fun present(): String {
            return this.name + " " + this.surname + " " + this.grade + " " + this.birthdayYear
        }
    }


}
