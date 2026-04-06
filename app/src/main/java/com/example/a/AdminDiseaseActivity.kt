package com.example.a

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminDiseaseActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button
    private lateinit var tvCount: TextView
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_disease)

        recyclerView = findViewById(R.id.recyclerViewDiseases)

        btnAdd = findViewById(R.id.buttonManageDisease)

        btnBack = findViewById(R.id.buttonDiseaseBack)
        tvCount = findViewById(R.id.textViewDiseaseCount)

        db = Database(applicationContext, "healthcare", null, 3)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadDiseases()

        btnAdd.setOnClickListener {
            showAddDiseaseDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadDiseases()
    }

    private fun loadDiseases() {
        val list = db.getAllDiseases() // List<Pair<name, description>>
        tvCount.text = "Tổng bệnh: ${list.size}"
        val adapter = DiseaseAdapter(list,
            onEdit = { pair -> showEditDiseaseDialog(pair) },
            onDelete = { name -> confirmDeleteDisease(name) }
        )
        recyclerView.adapter = adapter
    }

    private fun showAddDiseaseDialog() {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 20)

        val etName = EditText(this)
        etName.hint = "Tên bệnh"
        layout.addView(etName)

        val etDesc = EditText(this)
        etDesc.hint = "Mô tả"
        layout.addView(etDesc)

        AlertDialog.Builder(this)
            .setTitle("Thêm bệnh mới")
            .setView(layout)
            .setPositiveButton("Thêm") { _, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                if (name.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập tên bệnh", Toast.LENGTH_SHORT).show()
                } else {
                    db.addDisease(name, desc)
                    Toast.makeText(this, "Đã thêm $name", Toast.LENGTH_SHORT).show()
                    loadDiseases()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showEditDiseaseDialog(pair: Pair<String, String>) {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 20)

        val etName = EditText(this)
        etName.setText(pair.first)
        layout.addView(etName)

        val etDesc = EditText(this)
        etDesc.setText(pair.second)
        layout.addView(etDesc)

        AlertDialog.Builder(this)
            .setTitle("Sửa bệnh")
            .setView(layout)
            .setPositiveButton("Lưu") { _, _ ->
                val newName = etName.text.toString().trim()
                val newDesc = etDesc.text.toString().trim()
                if (newName.isEmpty()) {
                    Toast.makeText(this, "Tên không được trống", Toast.LENGTH_SHORT).show()
                } else {
                    db.updateDisease(pair.first, newName, newDesc)
                    Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show()
                    loadDiseases()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun confirmDeleteDisease(name: String) {
        AlertDialog.Builder(this)
            .setTitle("Xóa bệnh")
            .setMessage("Bạn có chắc muốn xóa \"$name\"?")
            .setPositiveButton("Xóa") { _, _ ->
                db.deleteDisease(name)
                Toast.makeText(this, "Đã xóa $name", Toast.LENGTH_SHORT).show()
                loadDiseases()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}