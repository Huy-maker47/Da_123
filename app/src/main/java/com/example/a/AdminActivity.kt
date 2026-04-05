package com.example.a

import android.content.Intent
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

class AdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnLogout: Button
    private lateinit var btnAddDisease: Button
    private lateinit var tvUserCount: TextView
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.recyclerViewUsers)
        btnLogout = findViewById(R.id.buttonAdminLogout)
        btnAddDisease = findViewById(R.id.buttonManageDisease)
        tvUserCount = findViewById(R.id.textViewUserCount)

        db = Database(applicationContext, "healthcare", null, 2)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadUsers()

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // ĐÂY — listener phải nằm trong onCreate
        btnAddDisease.setOnClickListener {
            showAddDiseaseDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }

    private fun loadUsers() {
        val userList = db.getAllUsers()
        tvUserCount.text = "Tổng người dùng: ${userList.size}"
        val adapter = UserAdapter(userList) { username -> confirmDelete(username) }
        recyclerView.adapter = adapter
    }

    private fun confirmDelete(username: String) {
        AlertDialog.Builder(this)
            .setTitle("Xóa người dùng")
            .setMessage("Bạn có chắc muốn xóa \"$username\"?")
            .setPositiveButton("Xóa") { _, _ ->
                db.deleteUser(username)
                Toast.makeText(this, "Đã xóa $username", Toast.LENGTH_SHORT).show()
                loadUsers()
            }
            .setNegativeButton("Hủy", null)
            .show()
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
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}