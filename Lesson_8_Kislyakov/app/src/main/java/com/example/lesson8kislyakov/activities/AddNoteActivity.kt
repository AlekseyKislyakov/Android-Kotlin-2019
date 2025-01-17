package com.example.lesson8kislyakov.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lesson8kislyakov.R
import com.example.lesson8kislyakov.adapters.ColorPickerAdapter
import com.example.lesson8kislyakov.db.Note
import com.example.lesson8kislyakov.decorators.GridItemDecoration
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.color_picker_recycler_view.view.*

const val COLOR_PICKER_COLUMN_COUNT = 4

class AddNoteActivity : AppCompatActivity() {

    var currentNote = Note("", "")

    companion object {
        fun newIntent(context: Context, note: Note?): Intent {
            return if (note != null) {
                val intent = Intent(context, AddNoteActivity::class.java)
                intent.putExtra(NOTE_EXTRA, note)
                intent
            } else Intent(context, AddNoteActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        toolbarAddNote.inflateMenu(R.menu.menu_add_note)

        if (intent.hasExtra(NOTE_EXTRA)) {
            currentNote = intent.getParcelableExtra(NOTE_EXTRA)
            // set up fields for edittext
            editTextAddNoteHeader.setText(currentNote.noteHeader)
            editTextAddNoteText.setText(currentNote.noteText)
            linearLayoutAddNote.setBackgroundColor(Color.parseColor(currentNote.noteColor))
        } else {
            // if new note, empty fields
            currentNote = Note("", "")
        }

        // save fields and finish activity
        toolbarAddNote.setNavigationOnClickListener {
            onBackPressed()
        }

        // colorpicker
        // create recyclerview with colors
        val mRecyclerView =
            layoutInflater.inflate(R.layout.color_picker_recycler_view, null)
        mRecyclerView.colorPickerRecyclerView.layoutManager =
            GridLayoutManager(
                this,
                COLOR_PICKER_COLUMN_COUNT
            )
        mRecyclerView.colorPickerRecyclerView.addItemDecoration(
            GridItemDecoration(
                (resources.getDimension(
                    R.dimen.padding_color_picker
                ).toInt()),
                COLOR_PICKER_COLUMN_COUNT
            )
        )
        val adapter = ColorPickerAdapter()
        // get colors list

        currentNote.noteColor?.let {
            adapter.setItems(
                this.resources.getStringArray(R.array.colorPickerColors).toList(),
                it
            )
        }
        adapter.onItemClick = { item ->
            // onclick apply chosen color
            currentNote.noteColor = item
            linearLayoutAddNote.setBackgroundColor(Color.parseColor(item))
        }
        mRecyclerView.colorPickerRecyclerView.adapter = adapter

        toolbarAddNote.setOnMenuItemClickListener {
            if (it.itemId == R.id.itemColorPicker) {
                // the idea is if user opens color dialog we're waiting for him to choose color and click ok
                // if we leave as in task (only cancel and color pick buttons) user won't be able to return
                // note to white color
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.choose_color))
                    .setView(mRecyclerView)
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialog, _ ->
                        // nothing do here
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
            true
        }
    }

    // override onBackPressed to save note
    override fun onBackPressed() {
        currentNote.noteHeader = editTextAddNoteHeader.text.toString()
        currentNote.noteText = editTextAddNoteText.text.toString()
        intent.putExtra(NOTE_EXTRA, currentNote)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}
