package com.example.smscrud

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "StudentDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE students(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER, course TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS students")
        onCreate(db)
    }

    fun addStudent(student: Student): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", student.name)
        values.put("age", student.age)
        values.put("course", student.course)
        val result = db.insert("students", null, values)
        db.close()
        return result != -1L
    }

    fun getAllStudents(): ArrayList<Student> {
        val list = ArrayList<Student>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM students", null)
        if (cursor.moveToFirst()) {
            do {
                val student = Student(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3)
                )
                list.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateStudent(student: Student): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", student.name)
        values.put("age", student.age)
        values.put("course", student.course)
        val result = db.update("students", values, "id=?", arrayOf(student.id.toString()))
        db.close()
        return result > 0
    }

    fun deleteStudent(id: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("students", "id=?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }
}
