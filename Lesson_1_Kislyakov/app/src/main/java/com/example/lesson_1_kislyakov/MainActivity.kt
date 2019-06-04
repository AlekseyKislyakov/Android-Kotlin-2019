package com.example.lesson_1_kislyakov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //create sorted list
        var studentsList = sortedSetOf<String>()

        //add list of items with space delimiter
        add_student_button.setOnClickListener {
            studentsList.addAll(input_edittext.text.toString().split(" "))
            input_edittext.text.clear()
            Toast.makeText(this, "Student added", Toast.LENGTH_SHORT)
                .show()
        }

        //create empty string to concat all the students, then show
        view_students_button.setOnClickListener {
            var str = StringBuilder()
            studentsList.forEach { str.append(it + '\n') }
            showlist_textview.text = str.toString()
        }

        // start activity 2
        goto2activity_button.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }

    }
}