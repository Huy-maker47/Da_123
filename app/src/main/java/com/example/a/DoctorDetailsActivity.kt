package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DoctorDetailsActivity : AppCompatActivity() {
    lateinit var list: ArrayList<HashMap<String, String>>
    lateinit var sa: SimpleAdapter
    lateinit var item: HashMap<String, String>
    val doctor_details1 = arrayOf(
        arrayOf("Bac si: Nguyen Van An", "Benh vien: Bach Mai - Ha Noi", "Kinh nghiem: 5 nam", "SDT: 0912345678", "600,000VND"),
        arrayOf("Bac si: Tran Thi Bich", "Benh vien: Cho Ray - TP.HCM", "Kinh nghiem: 10 nam", "SDT: 0987654321", "900,000VND"),
        arrayOf("Bac si: Le Van Cuong", "Benh vien: Da Nang", "Kinh nghiem: 7 nam", "SDT: 0909123456", "500,000VND"),
        arrayOf("Bac si: Pham Thi Dung", "Benh vien: Hue", "Kinh nghiem: 6 nam", "SDT: 0933123456", "400,000VND"),
        arrayOf("Bac si: Hoang Van Em", "Benh vien: Can Tho", "Kinh nghiem: 8 nam", "SDT: 0977123456", "700,000VND")
    )

    val doctor_details2 = arrayOf(
        arrayOf("Bac si: Nguyen Thi Hoa", "Benh vien: Bach Mai", "Kinh nghiem: 5 nam", "SDT: 0911223344", "600"),
        arrayOf("Bac si: Tran Van Khoa", "Benh vien: Cho Ray", "Kinh nghiem: 12 nam", "SDT: 0988776655", "1,000,000VND"),
        arrayOf("Bac si: Le Thi Lan", "Benh vien: Da Nang", "Kinh nghiem: 6 nam", "SDT: 0909000111", "500,000VND"),
        arrayOf("Bac si: Pham Van Minh", "Benh vien: Hue", "Kinh nghiem: 9 nam", "SDT: 0933555666", "800,000VND"),
        arrayOf("Bac si: Do Thi Nga", "Benh vien: Can Tho", "Kinh nghiem: 7 nam", "SDT: 0977888999", "700,000VND")
    )

    val doctor_details3 = arrayOf(
        arrayOf("Bac si: Nguyen Van Phuc", "Benh vien: Bach Mai", "Kinh nghiem: 4 nam", "SDT: 0911000001", "300,000VND"),
        arrayOf("Bac si: Tran Thi Quynh", "Benh vien: Cho Ray", "Kinh nghiem: 5 nam", "SDT: 0988000002", "400,000VND"),
        arrayOf("Bac si: Le Van Son", "Benh vien: Da Nang", "Kinh nghiem: 6 nam", "SDT: 0909000003", "500,000VND"),
        arrayOf("Bac si: Pham Thi Thao", "Benh vien: Hue", "Kinh nghiem: 7 nam", "SDT: 0933000004", "600,000VND"),
        arrayOf("Bac si: Hoang Van Tuan", "Benh vien: Can Tho", "Kinh nghiem: 8 nam", "SDT: 0977000005", "700,000VND")
    )

    val doctor_details4 = arrayOf(
        arrayOf("Bac si: Nguyen Van Long", "Benh vien: Bach Mai", "Kinh nghiem: 5 nam", "SDT: 0912111111", "600,000VND"),
        arrayOf("Bac si: Tran Thi Mai", "Benh vien: Cho Ray", "Kinh nghiem: 15 nam", "SDT: 0988222222", "1,200,000VND"),
        arrayOf("Bac si: Le Van Nam", "Benh vien: Da Nang", "Kinh nghiem: 9 nam", "SDT: 0909333333", "700,000VND"),
        arrayOf("Bac si: Pham Thi Oanh", "Benh vien: Hue", "Kinh nghiem: 6 nam", "SDT: 0933444444", "500,000VND"),
        arrayOf("Bac si: Do Van Phong", "Benh vien: Can Tho", "Kinh nghiem: 10 nam", "SDT: 0977555555", "900,000VND")
    )

    val doctor_details5 = arrayOf(
        arrayOf("Bac si: Nguyen Van Quy", "Benh vien: Bach Mai", "Kinh nghiem: 12 nam", "SDT: 0912666666", "1,500,000VND"),
        arrayOf("Bac si: Tran Thi Trang", "Benh vien: Cho Ray", "Kinh nghiem: 14 nam", "SDT: 0988777777", "1,700,000VND"),
        arrayOf("Bac si: Le Van Viet", "Benh vien: Da Nang", "Kinh nghiem: 11 nam", "SDT: 0909888888", "1,400,000VND"),
        arrayOf("Bac si: Pham Thi Xuan", "Benh vien: Hue", "Kinh nghiem: 13 nam", "SDT: 0933999999", "1,600,000VND"),
        arrayOf("Bac si: Hoang Van Yen", "Benh vien: Can Tho", "Kinh nghiem: 15 nam", "SDT: 0977000111", "1,800,000VND")
    )
    lateinit var tv: TextView
    lateinit var btn: Button
    lateinit var doctor_details: Array<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_doctor_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tv = findViewById(R.id.textView_logo6)
        btn = findViewById(R.id.buttonDDBack)

        val title = intent.getStringExtra("title")
        tv.text = title

        doctor_details = when (title) {
            "Family Physicians" -> doctor_details1
            "Dietician" -> doctor_details2
            "Dentist" -> doctor_details3
            "Surgeon" -> doctor_details4
            else -> doctor_details5
        }

        btn.setOnClickListener {
            startActivity(
                Intent(this@DoctorDetailsActivity, FindDoctorActivity::class.java))
    }
        val list = ArrayList<HashMap<String, String>>()

        for (i in doctor_details.indices) {
            val item = HashMap<String, String>()

            item["Line1"] = doctor_details[i][0]
            item["Line2"] = doctor_details[i][1]
            item["Line3"] = doctor_details[i][2]
            item["Line4"] = doctor_details[i][3]
            item["Line5"] = "Cons Fees: ${doctor_details[i][4]}/-"

            list.add(item)
        }
        val sa = SimpleAdapter(
            this,
            list,
            R.layout.multi_lines,
            arrayOf("Line1", "Line2", "Line3", "Line4", "Line5"),
            intArrayOf(R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e)
        )

// Gán ListView
        val lst = findViewById<ListView>(R.id.ListViewDD)
        lst.adapter = sa

        lst.setOnItemClickListener { _, _, i, _ ->
            val it = Intent(this@DoctorDetailsActivity, BookAppointmentActivity::class.java)
            it.putExtra("text1", title)
            it.putExtra("text2", doctor_details[i][0])
            it.putExtra("text3", doctor_details[i][1])
            it.putExtra("text4", doctor_details[i][3])
            it.putExtra("text5", doctor_details[i][4])
            startActivity(it)
        }

}
}