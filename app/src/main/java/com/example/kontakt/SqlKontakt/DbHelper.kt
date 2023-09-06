package com.example.lesson51sql.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.location.LocationRequestCompat.Quality
import com.example.contactapp.models.Kontakt

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), DatabaseServise {
    companion object {
        const val DATABASE_NAME = "kontakt.db"
        const val TABLE_NAME = "kontakt"
        const val DATABASE_VERSION = 1
        const val STUDENT_NAME = "name"
        const val PHONE_NUMBER = "phone_number"

    }

    override fun onCreate(newdb: SQLiteDatabase?) {
        // database yaratilgandan keyin ishga tushadi
        // autoicrement id create database
        val createTable =
            "CREATE TABLE $TABLE_NAME ($STUDENT_NAME TEXT, $PHONE_NUMBER TEXT)"
        newdb?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    override fun addKontakt(student: Kontakt) {
        val contentValues = ContentValues()
        contentValues.put(STUDENT_NAME, student.name)
        contentValues.put(PHONE_NUMBER, student.number)
        this.writableDatabase.insert(TABLE_NAME, null, contentValues)
    }


    @SuppressLint("Range", "Recycle", "SuspiciousIndentation")
    override fun getAllKontakt(): List<Kontakt> {
        val arraylist = ArrayList<Kontakt>()
        val database = this.readableDatabase
        val cursor = database.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(STUDENT_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(PHONE_NUMBER))
                arraylist.add(Kontakt(name=name, number = phoneNumber))
            } while (cursor.moveToNext())
        }
        return arraylist
    }
}