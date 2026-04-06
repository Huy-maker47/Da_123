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
import com.google.android.material.tabs.TabLayout

class AdminActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var layoutUsers: LinearLayout
    private lateinit var layoutDiseases: LinearLayout

    // Users
    private lateinit var recyclerViewUsers: RecyclerView
    private lateinit var tvUserCount: TextView

    // Diseases
    private lateinit var recyclerViewDiseases: RecyclerView
    private lateinit var tvDiseaseCount: TextView
    private lateinit var btnAddDisease: Button

    private lateinit var btnLogout: Button
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Bind views
        tabLayout = findViewById(R.id.tabLayout)
        layoutUsers = findViewById(R.id.layoutUsers)
        layoutDiseases = findViewById(R.id.layoutDiseases)
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers)
        tvUserCount = findViewById(R.id.textViewUserCount)
        recyclerViewDiseases = findViewById(R.id.recyclerViewDiseases)
        tvDiseaseCount = findViewById(R.id.textViewDiseaseCount)
        btnAddDisease = findViewById(R.id.buttonAddDisease)
        btnLogout = findViewById(R.id.buttonAdminLogout)

        db = Database(applicationContext, "healthcare", null, 3)

        recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        recyclerViewDiseases.layoutManager = LinearLayoutManager(this)

        // Setup tabs
        tabLayout.addTab(tabLayout.newTab().setText("Người dùng"))
        tabLayout.addTab(tabLayout.newTab().setText("Bệnh"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        layoutUsers.visibility = android.view.View.VISIBLE
                        layoutDiseases.visibility = android.view.View.GONE
                        loadUsers()
                    }
                    1 -> {
                        layoutUsers.visibility = android.view.View.GONE
                        layoutDiseases.visibility = android.view.View.VISIBLE
                        loadDiseases()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Load mặc định tab đầu
        loadUsers()

        btnAddDisease.setOnClickListener {
            showAddDiseaseDialog()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }

    private fun loadUsers() {
        val userList = db.getAllUsers()
        tvUserCount.text = "Tổng người dùng: ${userList.size}"
        val adapter = UserAdapter(userList) { username -> confirmDeleteUser(username) }
        recyclerViewUsers.adapter = adapter
    }

    private fun loadDiseases() {
        val list = db.getAllDiseases()
        tvDiseaseCount.text = "Tổng bệnh: ${list.size}"
        val adapter = DiseaseAdapter(list,
            onEdit = { pair -> showEditDiseaseDialog(pair) },
            onDelete = { name -> confirmDeleteDisease(name) }
        )
        recyclerViewDiseases.adapter = adapter
    }

    private fun confirmDeleteUser(username: String) {
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