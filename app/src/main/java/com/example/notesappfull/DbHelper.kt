package com.example.notesappfull

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DbHelper(context: Context): SQLiteOpenHelper(context,"notes.db", null, 2) {
    private val sqLiteDatabase: SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table notes (pk INTEGER PRIMARY KEY AUTOINCREMENT, Note text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun saveData(note: String){
        val contentValues = ContentValues()

        contentValues.put("Note", note)

        sqLiteDatabase.insert("notes", null, contentValues)
    }

    fun readData(): ArrayList<Note>{
        val noteList = arrayListOf<Note>()


        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes", null)

        if(cursor.count < 1){
            println("No Data Found")
        }else{
            while(cursor.moveToNext()){
                val pk = cursor.getInt(0)
                val note = cursor.getString(1)

                noteList.add(Note(pk, note))
            }
        }
        return noteList
    }

    fun updateData(note: Note){
        val contentValues = ContentValues()
        contentValues.put("Note", note.note)
        sqLiteDatabase.update("notes", contentValues, "pk = ${note.pk}", null)
    }

    fun deleteData(note: Note){
        sqLiteDatabase.delete("notes", "pk = ${note.pk}", null)
    }
}