package com.example.smscrud

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var studentList: ArrayList<Student>
    private lateinit var adapter: StudentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.btnAdd)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadStudents()

        btnAdd.setOnClickListener {
            showStudentDialog(null)
        }
    }

    private fun loadStudents() {
        studentList = db.getAllStudents()
        adapter = StudentAdapter(studentList, { student -> showStudentDialog(student) }, { student -> deleteStudent(student) })
        recyclerView.adapter = adapter
    }

    private fun showStudentDialog(student: Student?) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.item_student, null)
        val edtName = dialogView.findViewById<EditText>(R.id.edtName)
        val edtAge = dialogView.findViewById<EditText>(R.id.edtAge)
        val edtCourse = dialogView.findViewById<EditText>(R.id.edtCourse)

        if (student != null) {
            edtName.setText(student.name)
            edtAge.setText(student.age.toString())
            edtCourse.setText(student.course)
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(if (student == null) "Add Student" else "Update Student")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val name = edtName.text.toString()
                val age = edtAge.text.toString().toInt()
                val course = edtCourse.text.toString()

                if (student == null) {
                    db.addStudent(Student(0, name, age, course))
                } else {
                    db.updateStudent(Student(student.id, name, age, course))
                }
                loadStudents()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun deleteStudent(student: Student) {
        db.deleteStudent(student.id)
        loadStudents()
    }
}
