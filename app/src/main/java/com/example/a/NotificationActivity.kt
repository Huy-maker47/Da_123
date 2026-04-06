package com.example.a

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class NotificationActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var btnBack: Button
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        createNotificationChannel()

        recyclerView = findViewById(R.id.recyclerViewNotifications)
        btnAdd = findViewById(R.id.buttonAddNotification)
        btnBack = findViewById(R.id.buttonNotificationBack)

        db = Database(applicationContext, "healthcare", null, 3)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadNotifications()

        btnAdd.setOnClickListener { showAddNotificationDialog() }
        btnBack.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        loadNotifications()
    }

    private fun loadNotifications() {
        val list = db.getAllNotifications()
        val adapter = NotificationAdapter(list) { id, requestCode ->
            confirmDelete(id, requestCode)
        }
        recyclerView.adapter = adapter
    }

    private fun showAddNotificationDialog() {
        val diseases = db.getAllDiseases().map { it.first }
        if (diseases.isEmpty()) {
            Toast.makeText(this, "Chưa có bệnh nào trong database!", Toast.LENGTH_SHORT).show()
            return
        }

        // Bước 1: chọn bệnh
        AlertDialog.Builder(this)
            .setTitle("Chọn loại bệnh")
            .setItems(diseases.toTypedArray()) { _, index ->
                val selectedDisease = diseases[index]
                showNoteAndTimeDialog(selectedDisease)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showNoteAndTimeDialog(diseaseName: String) {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 20)

        val etNote = EditText(this)
        etNote.hint = "Ghi chú"
        layout.addView(etNote)

        val tvTime = TextView(this)
        tvTime.text = "Chưa chọn giờ"
        tvTime.textSize = 14f
        tvTime.setPadding(0, 12, 0, 0)
        layout.addView(tvTime)

        val btnPickTime = Button(this)
        btnPickTime.text = "Chọn giờ thông báo"
        layout.addView(btnPickTime)

        var selectedTimeMillis = 0L

        btnPickTime.setOnClickListener {
            val cal = Calendar.getInstance()
            TimePickerDialog(this, { _, hour, minute ->
                val picked = Calendar.getInstance()
                picked.set(Calendar.HOUR_OF_DAY, hour)
                picked.set(Calendar.MINUTE, minute)
                picked.set(Calendar.SECOND, 0)
                picked.set(Calendar.MILLISECOND, 0)
                // Nếu giờ đã qua hôm nay thì chuyển sang ngày mai
                if (picked.timeInMillis <= System.currentTimeMillis()) {
                    picked.add(Calendar.DAY_OF_MONTH, 1)
                }
                selectedTimeMillis = picked.timeInMillis
                tvTime.text = "Giờ: ${String.format("%02d:%02d", hour, minute)}"
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        AlertDialog.Builder(this)
            .setTitle("Thông báo: $diseaseName")
            .setView(layout)
            .setPositiveButton("Đặt thông báo") { _, _ ->
                val note = etNote.text.toString().trim()
                if (selectedTimeMillis == 0L) {
                    Toast.makeText(this, "Vui lòng chọn giờ!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                db.addNotification(diseaseName, note, selectedTimeMillis)
                scheduleNotification(diseaseName, note, selectedTimeMillis)
                Toast.makeText(this, "Đã đặt thông báo!", Toast.LENGTH_SHORT).show()
                loadNotifications()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun scheduleNotification(diseaseName: String, note: String, timeMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("disease", diseaseName)
            putExtra("note", note)
        }
        val requestCode = timeMillis.toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            this, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Dùng set thay vì setExactAndAllowWhileIdle — không cần permission đặc biệt
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeMillis, pendingIntent)
    }

    private fun confirmDelete(id: Int, requestCode: Int) {
        AlertDialog.Builder(this)
            .setTitle("Xóa thông báo")
            .setMessage("Hủy thông báo này?")
            .setPositiveButton("Xóa") { _, _ ->
                // Hủy alarm
                val intent = Intent(this, NotificationReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this, requestCode, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                // Xóa DB
                db.deleteNotification(id)
                Toast.makeText(this, "Đã hủy thông báo", Toast.LENGTH_SHORT).show()
                loadNotifications()
            }
            .setNegativeButton("Không", null)
            .show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "health_channel",
                "Health Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}