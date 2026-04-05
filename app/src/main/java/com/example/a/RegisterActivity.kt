package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    lateinit var edUsername: EditText
    lateinit var edEmail: EditText
    lateinit var edPassword: EditText
    lateinit var edConfirm: EditText
    lateinit var btn: Button
    lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edUsername = findViewById(R.id.editTextRegUsername)
        edEmail = findViewById(R.id.editTextRegEmail)
        edPassword = findViewById(R.id.editTextRegPassword)
        edConfirm = findViewById(R.id.editRegConfirmPassword)
        btn = findViewById(R.id.ButtonRegister)
        tv = findViewById(R.id.textViewExistingUser)
        val db = Database(applicationContext, "healthcare", null, 1)
        tv.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        btn.setOnClickListener {
            val username = edUsername.text.toString()
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()
            val confirm = edConfirm.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill All details", Toast.LENGTH_SHORT).show()
            } else {
                if (password == confirm) {
                    if (isValid(password)) {
                        db.register(username, email, password)
                        Toast.makeText(
                            applicationContext,
                            "Record Inserted",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))

                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Password must contain at least 8 characters, having letter, digit and special symbol",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Password and Confirm password didn't match",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun isValid(password: String): Boolean {
        var hasLetter = false
        var hasDigit = false
        var hasSpecial = false

        if (password.length < 8) return false

        for (c in password) {
            if (c.isLetter()) hasLetter = true
            else if (c.isDigit()) hasDigit = true
            else if (c in '!'..'.' || c == '@') hasSpecial = true
        }

        return hasLetter && hasDigit && hasSpecial
    }
}