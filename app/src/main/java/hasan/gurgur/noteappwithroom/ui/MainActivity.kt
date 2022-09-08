package hasan.gurgur.noteappwithroom.ui

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import hasan.gurgur.noteappwithroom.R
import hasan.gurgur.noteappwithroom.adapter.NoteAdapter
import hasan.gurgur.noteappwithroom.databinding.ActivityMainBinding
import hasan.gurgur.noteappwithroom.db.NoteDatabase
import hasan.gurgur.noteappwithroom.utils.Constants.NOTE_DATABASE

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val noteDB: NoteDatabase by lazy {
        Room.databaseBuilder(this, NoteDatabase::class.java, NOTE_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    private val noteAdapter by lazy { NoteAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }


    }

    override fun onResume() {
        super.onResume()
        checkItem()
    }

    private fun checkItem() {
        binding.apply {

            if (noteDB.doa().getAllNotes().isNotEmpty()) {
                rvNoteList.visibility = View.VISIBLE
                tvEmptyText.visibility = View.GONE
                noteAdapter.differ.submitList(noteDB.doa().getAllNotes())
                setupRecyclerView()
            } else {
                rvNoteList.visibility = View.GONE
                tvEmptyText.visibility = View.VISIBLE


            }
        }
    }

    private fun setupRecyclerView() {

        binding.rvNoteList.apply {

            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }
    }
}