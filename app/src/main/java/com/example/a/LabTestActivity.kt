package com.example.a

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

private val packages = arrayOf(
    arrayOf("Package 1 : Full Body Checkup", "999"),
    arrayOf("Package 2 : Blood Glucose Fasting", "299"),
    arrayOf("Package 3 : COVID-19 Antibody - IgG", "899"),
    arrayOf("Package 4 : Thyroid Check", "499"),
    arrayOf("Package 5 : Immunity Check", "699")
)

private val package_details = arrayOf(
    """Blood Glucose Fasting
Complete Hemogram
HbA1c
Iron Studies
Kidney Function Test
LDH Lactate Dehydrogenase, Serum
Lipid Profile
Liver Function Test""",
    "Blood Glucose Fasting",
    "COVID-19 Antibody - IgG",
    "Thyroid Profile-Total (T3, T4 & TSH Ultra-sensitive)",
    """Complete Hemogram
CRP (C Reactive Protein) Quantitative, Serum
Iron Studies
Kidney Function Test
Vitamin D Total-25 Hydroxy
Liver Function Test
Lipid Profile"""
)
private lateinit var btnGoToCart: Button
private lateinit var btnBack: Button
private lateinit var listView: ListView
private lateinit var sa: SimpleAdapter
// ArrayList nên được xác định kiểu dữ liệu cụ thể để tránh lỗi
private var list = ArrayList<HashMap<String, String>>()
class LabTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnGoToCart = findViewById(R.id.buttonAddToCart)
        btnBack = findViewById(R.id.buttonLDBack)
        listView = findViewById(R.id.editTextLDTextMultiLine)

        // 2. Xử lý nút Quay lại
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        list = ArrayList()

        for (i in packages.indices) {
            val item = HashMap<String, String>()
            item["line1"] = packages[i][0]
            item["line2"] = packages[i][1]
            item["line3"] = packages[i][2]
            item["line4"] = packages[i][3]
            item["line5"] = "Total Cost: ${packages[i][4]}/-"
            list.add(item)
        }
        sa = SimpleAdapter(
            this,
            list,
            R.layout.multi_lines,
            arrayOf("line1", "line2", "line3", "line4", "line5"),
            intArrayOf(R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e)
        )
        listView.adapter = sa
        listView.setOnItemClickListener { _, _, i, _ ->
            val it = Intent(this@LabTestActivity, LabTestDetailsActivity::class.java)

            // Truyền dữ liệu sang màn hình chi tiết
            it.putExtra("text1", packages[i][0])         // Tên gói (Package Name)
            it.putExtra("text2", package_details[i])     // Chi tiết (Details)
            it.putExtra("text3", packages[i][4])         // Giá tiền (Price)

            startActivity(it)
        }
    }
}