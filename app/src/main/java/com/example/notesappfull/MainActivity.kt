package com.example.notesappfull

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity: AppCompatActivity() {
    val dbHelper by lazy { DbHelper(applicationContext) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RVAdapter
    private lateinit var etNote:EditText
    private lateinit var addButton : FloatingActionButton
    private lateinit var noteList: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteList = arrayListOf()
        recyclerView = findViewById(R.id.recyclerView)
        etNote = findViewById(R.id.etNote)
        addButton = findViewById(R.id.addButton)

        recyclerViewAdapter = RVAdapter(noteList,this)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        readNote()

        addButton.setOnClickListener {
            if (!etNote.text.isNullOrEmpty()) {
                val note = etNote.text.toString()
                dbHelper.saveData(note)
                Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show()
                readNote()
            }
        }
    }

    private fun readNote() {
        noteList = dbHelper.readData()
        recyclerViewAdapter.update(noteList)
    }

    fun raiseDialog(pk: Int) {

        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint ="Update your note"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {
                    _, _ -> editNote(pk, updatedNote.text.toString())
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)

        alert.show()
    }

    private fun editNote(pk: Int, note: String) {
        dbHelper.updateData(Note(pk,note))
        Toast.makeText(this, "Updated successfully", Toast.LENGTH_LONG).show()
        readNote()
    }

    fun deleteNote(pk: Int) {

        dbHelper.deleteData(Note(pk,""))
        readNote()
    }
}