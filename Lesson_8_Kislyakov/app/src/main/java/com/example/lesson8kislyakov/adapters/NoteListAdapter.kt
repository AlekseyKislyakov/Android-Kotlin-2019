package com.example.lesson8kislyakov.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson8kislyakov.db.Note
import com.example.lesson8kislyakov.R
import com.example.lesson8kislyakov.activities.ACTIVITY_TILE_REQUEST_CODE
import com.example.lesson8kislyakov.activities.AddNoteActivity
import com.example.lesson8kislyakov.activities.STATUS_HIDDEN
import kotlinx.android.synthetic.main.note_layout.view.*

class NoteListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Note) -> Unit)? = null
    var onItemLongClick: ((Note) -> Unit)? = null

    private var noteList: List<Note> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_layout, parent, false)
        return NoteListViewHolder(view)
    }


    fun setItems(elements: List<Note>) {
        noteList = elements
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as NoteListViewHolder
        viewHolder.bind(noteList[position])
    }

    inner class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(element: Note) {
            itemView.setOnClickListener {
                onItemClick?.invoke(element)
            }

            itemView.setOnLongClickListener {
                onItemLongClick?.invoke(element)
                true
            }

            if (element.noteHeader == "") {
                itemView.textViewNoteHeader?.visibility = View.GONE
            } else {
                itemView.textViewNoteHeader?.visibility = View.VISIBLE
                itemView.textViewNoteHeader?.text = element.noteHeader
            }

            itemView.textViewNoteDescription?.text = element.noteText

            if (element.noteColor != null && element.noteColor != "") {
                itemView.cardViewLayoutNote.setCardBackgroundColor(Color.parseColor(element.noteColor))
            } else {
                itemView.cardViewLayoutNote.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.colorWhite
                    )
                )
            }

        }
    }
}