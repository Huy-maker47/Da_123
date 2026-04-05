package com.example.a

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FindDoctorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_find_doctor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val exit = findViewById<CardView>(R.id.cardFDBack)

        exit.setOnClickListener {
            startActivity(Intent(this@FindDoctorActivity, HomeActivity::class.java))
        }
        val familyphysician = findViewById<CardView>(R.id.cardFDFamilyPhysician)

        familyphysician.setOnClickListener {

            val it = Intent(this@FindDoctorActivity, DoctorDetailsActivity::class.java)
            it.putExtra("title", "Family Physicians")

            startActivity(it)
        }

        val dietician = findViewById<CardView>(R.id.cardFDDietician)
        dietician.setOnClickListener {
            startActivity(
                Intent(this@FindDoctorActivity, DoctorDetailsActivity::class.java)
                    .putExtra("title", "Dietician")
            )
        }

        val dentist = findViewById<CardView>(R.id.cardFDDentist)
        dentist.setOnClickListener {
            startActivity(
                Intent(this@FindDoctorActivity, DoctorDetailsActivity::class.java)
                    .putExtra("title", "Dentist")
            )
        }

        val surgeon = findViewById<CardView>(R.id.cardFDSurgeon)
        surgeon.setOnClickListener {
            startActivity(
                Intent(this@FindDoctorActivity, DoctorDetailsActivity::class.java)
                    .putExtra("title", "Surgeon")
            )
        }
        val cardiologists = findViewById<CardView>(R.id.cardFDCardiologists)

        cardiologists.setOnClickListener {
            startActivity(
                Intent(this@FindDoctorActivity, DoctorDetailsActivity::class.java)
                    .putExtra("title", "Cardiologists")
            )
        }
    }
}