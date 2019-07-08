package com.example.lesson8kislyakov

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.note_layout.view.*

class NoteListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Note) -> Unit)? = null
    var onItemLongClick: ((Note) -> Unit)? = null

    private var noteList: List<Note>? = ArrayList()

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
        if (noteList != null) {
            return noteList!!.size
        } else throw Exception("Must use setItems()!")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as NoteListViewHolder
        viewHolder.bind(noteList!![position])
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