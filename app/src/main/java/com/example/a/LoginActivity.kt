package com.example.a

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    lateinit var edUsername: EditText
    lateinit var edPassword: EditText
    lateinit var btn: Button
    lateinit var tv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        edUsername = findViewById (R.id.editTextLoginUsername)
        edPassword = findViewById(R.id.editTextLoginPassword)
        btn = findViewById(R.id.ButtonLogin)
        tv = findViewById(R.id.TextViewNewUser)


        val db = Database(applicationContext, "healthcare", null, 1)
        btn.setOnClickListener {
            val username = edUsername.text.toString()
            val password = edPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill All details",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                if (db.login(username, password) == 1) {

                    Toast.makeText(
                        applicationContext,
                        "Login Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.putString("username", username)
                    editor.apply()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))

                } else {

                    Toast.makeText(
                        applicationContext,
                        "Invalid Username and Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        tv.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}