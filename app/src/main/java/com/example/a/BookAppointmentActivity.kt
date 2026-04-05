package com.example.a

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import java.util.Calendar

class BookAppointmentActivity : AppCompatActivity() {
   private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    lateinit var dateButton: Button
lateinit var timeButton: Button
    lateinit var ed1: EditText
    lateinit var ed2: EditText
    lateinit var ed3: EditText
    lateinit var ed4: EditText
    lateinit var tv: TextView
    lateinit var btnBook: Button
    lateinit var btnBack: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_appointment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        tv = findViewById(R.id.textViewAppTitle)
        ed1 = findViewById(R.id.editTextAppFullName)
        ed2 = findViewById(R.id.editTextAppAddress)
        ed3 = findViewById(R.id.editTextAppContactNumber)
        ed4 = findViewById(R.id.editTextAppFees)
        dateButton = findViewById(R.id.buttonAppDate)
        timeButton = findViewById(R.id.buttonAppTime)

        btnBook = findViewById(R.id.buttonBookAppointment)
        btnBack = findViewById(R.id.buttonAppBack)
        initDatePicker()
        initTimePicker()
        dateButton.setOnClickListener {
            datePickerDialog.show()
        }

        timeButton.setOnClickListener {
            timePickerDialog.show()
        }
        btnBack.setOnClickListener {
            val intent = Intent(this, FindDoctorActivity::class.java)
            startActivity(intent)
        }
        btnBook.setOnClickListener {
            Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show()
            finish()
        }
        ed1.keyListener = null
        ed2.keyListener = null
        ed3.keyListener = null
        ed4.keyListener = null

        val it = intent
        val title = it.getStringExtra("text1")
        val fullname = it.getStringExtra("text2")
        val address = it.getStringExtra("text3")
        val contact = it.getStringExtra("text4")
        val fees = it.getStringExtra("text5")

        tv.text = title
        ed1.setText(fullname)
        ed2.setText(address)
        ed3.setText(contact)
        ed4.setText(fees)
        tv.text = title
        ed1.setText(fullname)
        ed2.setText(address)
        ed3.setText(contact)
        ed4.setText("Cons Fees: $fees/-")

        initDatePicker()

        dateButton.setOnClickListener {
            datePickerDialog.show()
        }

    }
    private fun initDatePicker() {

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val m = month + 1
            dateButton.text = "$day/$m/$year"
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_DARK

        datePickerDialog = DatePickerDialog(
            this,
            style,
            dateSetListener,
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = cal.timeInMillis + 86400000
    }
    private fun initTimePicker() {

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val formattedTime = String.format("%02d:%02d", hour, minute)
            timeButton.text = formattedTime
        }


        val cal = Calendar.getInstance()
        val hrs = cal.get(Calendar.HOUR_OF_DAY)
        val mins = cal.get(Calendar.MINUTE)

        val style = AlertDialog.THEME_HOLO_DARK

        timePickerDialog = TimePickerDialog(
            this,
            style,
            timeSetListener,
            hrs,
            mins,
            true
        )
    }
}