package com.example.lesson8kislyakov.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "notes_table")
data class Note(
    @field:ColumnInfo(name = "note_header")
    var noteHeader: String? = null,

    @field:ColumnInfo(name = "note_description")
    var noteText: String?,

    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,

    @ColumnInfo(name = "note_color")
    var noteColor: String? = "#ffffff",

    @ColumnInfo(name = "note_hidden")
    var hidden: Int = 0
) : Parcelable {
    fun isEmpty():Boolean{
        return (this.noteHeader == "" && this.noteColor == "#ffffff" && this.noteText == "")
    }
}


