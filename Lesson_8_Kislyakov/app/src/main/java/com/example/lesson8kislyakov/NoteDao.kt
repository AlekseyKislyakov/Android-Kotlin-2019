package com.example.lesson8kislyakov

import androidx.room.*
import com.example.lesson8kislyakov.Note
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface NoteDao {

    @get:Query("SELECT * FROM notes_table")
    val getAll: List<Note>

    @get:Query("SELECT * FROM notes_table")
    val getAllFlowable: Flowable<List<Note>>

    @Query("SELECT * FROM notes_table WHERE note_hidden = :status")
    fun getFlowableNotHidden(status: Int): Flowable<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM notes_table WHERE noteId = :id")
    fun getById(id: Long): Flowable<Note>

    @Query("SELECT * FROM notes_table WHERE note_hidden = :status AND (note_header LIKE :header OR note_description LIKE :header)")
    fun searchFlowableQuery(header: String,  status:Int): Flowable<List<Note>>
}