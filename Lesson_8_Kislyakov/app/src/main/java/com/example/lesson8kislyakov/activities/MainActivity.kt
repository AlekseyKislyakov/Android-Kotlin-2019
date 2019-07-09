package com.example.lesson8kislyakov.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.room.Room
import com.example.lesson8kislyakov.*
import com.example.lesson8kislyakov.adapters.NoteListAdapter
import com.example.lesson8kislyakov.db.Note
import com.example.lesson8kislyakov.db.NoteDatabase
import com.example.lesson8kislyakov.decorators.StaggeredItemDecoration
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
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
    var noteDatabase: NoteDatabase? = null
    private var disposable: Disposable? = null
    private var disposableSearch: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val noteListAdapter = NoteListAdapter()
        recyclerViewMain.adapter = noteListAdapter
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

        toolbarMain.inflateMenu(R.menu.menu_main)

        val searchView = toolbarMain.menu.findItem(R.id.itemSearch).actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        disposableSearch = Flowable.create(FlowableOnSubscribe<Flowable<List<Note>>> { subscriber ->
            searchView.setOnQueryTextListener(object :
                SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val result = "%$query%"
                    viewFlipperMain.displayedChild =
                        STATE_LOADING
                    subscriber.onNext(
                        noteDatabase!!.noteDao().searchFlowableQuery(
                            result,
                            STATUS_SHOWN
                        )
                    )
                    Log.d("mytag", query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val result = "%$newText%"
                    subscriber.onNext(
                        noteDatabase!!.noteDao().searchFlowableQuery(
                            result,
                            STATUS_SHOWN
                        )
                    )
                    Log.d("mytag", newText)
                    return false
                }
            })
        }, BackpressureStrategy.DROP).subscribe { it ->
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if(it.isEmpty()){
                        viewFlipperMain.displayedChild =
                            STATE_ERROR
                    }else{
                        viewFlipperMain.displayedChild =
                            STATE_DATA
                    }
                    noteListAdapter.setItems(it)
                }
        }

        disposable = noteDatabase!!.noteDao().getFlowableNotHidden(STATUS_SHOWN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                if(it.isEmpty()){
                    viewFlipperMain.displayedChild =
                        STATE_ERROR
                }else{
                    viewFlipperMain.displayedChild =
                        STATE_DATA
                }
                noteListAdapter.setItems(it)
                noteListAdapter.onItemClick = {
                    startActivityForResult(
                        AddNoteActivity.newIntent(
                            this,
                            it
                        ),
                        ACTIVITY_TILE_REQUEST_CODE
                    )
                }
                noteListAdapter.onItemLongClick = {

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Что делать с заметкой")
                        .setCancelable(true)
                        .setPositiveButton("Архивировать") { dialog, _ ->
                            dialog.cancel()
                            it.hidden = STATUS_HIDDEN
                            Flowable.fromCallable {
                                noteDatabase!!.noteDao().update(it)
                            }.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()

                        }.setNegativeButton("Удалить") { dialog, _ ->
                            dialog.cancel()
                            Flowable.fromCallable {
                                noteDatabase!!.noteDao().delete(it)
                            }.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
                        }.create()
                        .show()
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable?.isDisposed!!) {
            disposable!!.dispose()
        }
        if (!disposableSearch?.isDisposed!!) {
            disposableSearch!!.dispose()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_TILE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.hasExtra(NOTE_EXTRA)) {
                    val note = data.getParcelableExtra<Note>(NOTE_EXTRA)
                    if (!note.isEmpty()) {
                        Flowable.fromCallable {
                            noteDatabase!!.noteDao().insert(note)
                            return@fromCallable
                        }.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
                    }
                }

            }
        }

    }


}
