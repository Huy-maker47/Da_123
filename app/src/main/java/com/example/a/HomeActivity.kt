package com.example.a

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "").toString()
        Toast.makeText(applicationContext, "Welcome $username", Toast.LENGTH_SHORT).show()

        // Logout
        findViewById<CardView>(R.id.cardExit).setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Find Doctor
        findViewById<CardView>(R.id.cardFindDoctor).setOnClickListener {
            startActivity(Intent(this, FindDoctorActivity::class.java))
        }

        // Lab Test
        findViewById<CardView>(R.id.cardLabTest).setOnClickListener {
            startActivity(Intent(this, LabTestActivity::class.java))
        }

        // Disease
        findViewById<CardView>(R.id.cardDisease).setOnClickListener {
            startActivity(Intent(this, DiseaseActivity::class.java))
        }
    }
}