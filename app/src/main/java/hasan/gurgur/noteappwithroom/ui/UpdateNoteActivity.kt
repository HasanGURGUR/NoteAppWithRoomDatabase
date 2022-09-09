package hasan.gurgur.noteappwithroom.ui

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import hasan.gurgur.noteappwithroom.R
import hasan.gurgur.noteappwithroom.databinding.ActivityUpdateNoteBinding
import hasan.gurgur.noteappwithroom.db.NoteDatabase
import hasan.gurgur.noteappwithroom.db.NoteEntity
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import hasan.gurgur.noteappwithroom.utils.Constants.BUNDLE_NOTE_ID
import hasan.gurgur.noteappwithroom.utils.Constants.NOTE_DATABASE

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private val noteDB: NoteDatabase by lazy {
        Room.databaseBuilder(this, NoteDatabase::class.java, NOTE_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private lateinit var noteEntity: NoteEntity
    private var noteId = 0
    private var defaultTitle = ""
    private var defaultDesc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            noteId = it.getInt(BUNDLE_NOTE_ID)
        }

        binding.apply {
            defaultTitle = noteDB.doa().getNote(noteId).noteTitle
            defaultDesc = noteDB.doa().getNote(noteId).noteDesc

            edtTitle.setText(defaultTitle)
            edtDesc.setText(defaultDesc)


            btnDelete.setOnClickListener {

                showCustomAlertDialog()

            }

            btnSave.setOnClickListener {
                val title = edtTitle.text.toString()
                val desc = edtDesc.text.toString()

                if (title.isNotEmpty() || desc.isNotEmpty()) {

                    noteEntity = NoteEntity(noteId, title, desc)
                    noteDB.doa().updateNote(noteEntity)
                    finish()
                } else {
                    Snackbar.make(
                        it,
                        "Title and Describition cannot be Empty",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showCustomAlertDialog(){

        val builder = AlertDialog.Builder(this,R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.custom_view_layout,null)
        val negative_button = view.findViewById<Button>(R.id.dialog_neg_btn)
        val  positive_button = view.findViewById<Button>(R.id.dialog_pos_btn)
        builder.setView(view)
        positive_button.setOnClickListener {
            noteEntity = NoteEntity(noteId,defaultTitle,defaultDesc)
            noteDB.doa().deleteNote(noteEntity)
            finish()
        }
        negative_button.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

}