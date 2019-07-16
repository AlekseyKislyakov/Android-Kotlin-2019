package com.example.lesson8kislyakov.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.Room
import com.example.lesson8kislyakov.R
import com.example.lesson8kislyakov.adapters.NoteListAdapter
import com.example.lesson8kislyakov.db.Note
import com.example.lesson8kislyakov.db.NoteDatabase
import com.example.lesson8kislyakov.decorators.StaggeredItemDecoration
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

const val ACTIVITY_TILE_REQUEST_CODE: Int = 101
const val NOTE_EXTRA = "note_extra"
const val NOTE_COLUMN_COUNT = 2

const val DATABASE_NAME = "database"

const val STATUS_SHOWN = 0
const val STATUS_HIDDEN = 1

const val STATE_LOADING = 0
const val STATE_DATA = 1
const val STATE_ERROR = 2


class MainActivity : AppCompatActivity() {

    lateinit var noteDatabase: NoteDatabase

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val noteListAdapter = NoteListAdapter()

        toolbarMain.inflateMenu(R.menu.menu_main)
        val searchView = toolbarMain.menu.findItem(R.id.itemSearch).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        recyclerViewMain.adapter = noteListAdapter
        setNoteAdapterListeners(noteListAdapter)

        recyclerViewMain.layoutManager =
            StaggeredGridLayoutManager(NOTE_COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewMain.addItemDecoration(
            StaggeredItemDecoration(
                resources.getDimension(R.dimen.padding_note_list).toInt()
            )
        )

        noteDatabase = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            DATABASE_NAME
        )
            .build()

        val disposableSearch = createSearchDisposable(noteListAdapter, searchView)
        val disposable = createAllNotesDisposable(noteListAdapter)

        compositeDisposable.addAll(disposableSearch, disposable)

        floatingActionButtonMain.setOnClickListener {
            startActivityForResult(
                AddNoteActivity.newIntent(
                    this,
                    null
                ),
                ACTIVITY_TILE_REQUEST_CODE
            )
        }
    }

    private fun createSearchDisposable(noteListAdapter: NoteListAdapter, searchView: SearchView): Disposable {
        return Flowable.create(FlowableOnSubscribe<Flowable<List<Note>>> { subscriber ->
            searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val result = "%$query%"
                    viewFlipperMain.displayedChild =
                        STATE_LOADING
                    subscriber.onNext(
                        noteDatabase.noteDao().searchFlowableQuery(
                            result,
                            STATUS_SHOWN
                        )
                    )
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val result = "%$newText%"
                    subscriber.onNext(
                        noteDatabase.noteDao().searchFlowableQuery(
                            result,
                            STATUS_SHOWN
                        )
                    )
                    return false
                }
            })
        }, BackpressureStrategy.DROP).subscribe { it ->
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    rxSetItems(it, noteListAdapter)
                }
        }
    }

    private fun createAllNotesDisposable(noteListAdapter: NoteListAdapter): Disposable {
        return noteDatabase.noteDao().getFlowableNotHidden(STATUS_SHOWN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { noteList ->
                rxSetItems(noteList, noteListAdapter)
            }
    }

    private fun setNoteAdapterListeners(noteListAdapter: NoteListAdapter) {
        noteListAdapter.onItemClick = { note ->
            startActivityForResult(
                AddNoteActivity.newIntent(
                    this,
                    note
                ),
                ACTIVITY_TILE_REQUEST_CODE
            )
        }
        noteListAdapter.onItemLongClick = { note ->

            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.long_click_dialog_header))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.long_click_dialog_archive)) { dialog, _ ->
                    dialog.cancel()
                    note.hidden = STATUS_HIDDEN
                    rxDoSomething(note, ::update)

                }
                .setNegativeButton(getString(R.string.long_click_dialog_delete)) { dialog, _ ->
                    dialog.cancel()
                    rxDoSomething(note, ::delete)
                }.create()
                .show()
        }
    }

    fun insert(note: Note){
        noteDatabase.noteDao().insert(note)
    }

    fun delete(note: Note){
        noteDatabase.noteDao().delete(note)
    }

    fun update(note: Note){
        noteDatabase.noteDao().update(note)
    }


    private fun rxDoSomething(note: Note, function: (note: Note) -> Unit){
        Flowable.fromCallable {
            function(note)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun rxInsertDatabaseElement(note: Note) {
        Flowable.fromCallable {
            noteDatabase.noteDao().insert(note)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun rxUpdateDatabaseElement(note: Note) {
        Flowable.fromCallable {
            noteDatabase.noteDao().update(note)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun rxDeleteDatabaseElement(note: Note) {
        Flowable.fromCallable {
            noteDatabase.noteDao().delete(note)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun rxSetItems(noteList: List<Note>, adapter: NoteListAdapter) {
        if (noteList.isEmpty()) {
            viewFlipperMain.displayedChild =
                STATE_ERROR
        } else {
            viewFlipperMain.displayedChild =
                STATE_DATA
        }
        adapter.setItems(noteList)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_TILE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    if (data.hasExtra(NOTE_EXTRA)) {
                        val note = data.getParcelableExtra<Note>(NOTE_EXTRA)
                        if (!note.isEmpty()) {
                            rxDoSomething(note, ::insert)
                        }
                    }
                }

            }
        }

    }


}
