package com.example.a

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class DiseaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disease)

        val db = Database(applicationContext, "healthcare", null, 2)
        val diseases = db.getAllDiseases() // List<Pair<name, description>>

        val displayList = diseases.map { "${it.first} — ${it.second}" }

        val listView = findViewById<ListView>(R.id.listViewDiseases)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        listView.adapter = adapter
    }
}