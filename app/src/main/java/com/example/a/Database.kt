package com.example.a

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        // Bảng users
        db.execSQL("CREATE TABLE users(username TEXT, email TEXT, password TEXT)")

        // Bảng diseases
        db.execSQL("CREATE TABLE diseases(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT)")
        insertDefaultDiseases(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Khi tăng version từ 1 lên 2: tạo bảng diseases
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS diseases(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT)")
            insertDefaultDiseases(db)
        }
    }
    fun addDisease(name: String, description: String) {
        val cv = ContentValues()
        cv.put("name", name)
        cv.put("description", description)
        val db = writableDatabase
        db.insert("diseases", null, cv)
        db.close()
    }

    private fun insertDefaultDiseases(db: SQLiteDatabase) {
        val diseases = listOf(
            Pair("Flu", "Influenza - viral respiratory illness"),
            Pair("Diabetes", "Chronic condition affecting blood sugar"),
            Pair("Hypertension", "High blood pressure condition"),
            Pair("Asthma", "Chronic lung disease"),
            Pair("COVID-19", "Coronavirus infectious disease"),
            Pair("Dengue", "Mosquito-borne viral infection"),
            Pair("Tuberculosis", "Bacterial infection affecting lungs"),
            Pair("Malaria", "Mosquito-borne parasitic disease")
        )
        diseases.forEach { (name, desc) ->
            val cv = ContentValues()
            cv.put("name", name)
            cv.put("description", desc)
            db.insert("diseases", null, cv)
        }
    }

    fun register(username: String, email: String, password: String) {
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("email", email)
        cv.put("password", password)
        val db = writableDatabase
        db.insert("users", null, cv)
        db.close()
    }

    fun login(username: String, password: String): Int {
        var result = 0
        val db = readableDatabase
        val c = db.rawQuery(
            "SELECT * FROM users WHERE username = ? AND password = ?",
            arrayOf(username, password)
        )
        if (c.moveToFirst()) result = 1
        c.close()
        db.close()
        return result
    }

    fun getAllUsers(): MutableList<Triple<String, String, String>> {
        val list = mutableListOf<Triple<String, String, String>>()
        val db = readableDatabase
        val c = db.rawQuery("SELECT username, email, password FROM users", null)
        while (c.moveToNext()) {
            list.add(Triple(c.getString(0), c.getString(1), c.getString(2)))
        }
        c.close()
        db.close()
        return list
    }

    fun deleteUser(username: String) {
        val db = writableDatabase
        db.delete("users", "username = ?", arrayOf(username))
        db.close()
    }

    fun getAllDiseases(): MutableList<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val c = db.rawQuery("SELECT name, description FROM diseases", null)
        while (c.moveToNext()) {
            list.add(Pair(c.getString(0), c.getString(1)))
        }
        c.close()
        db.close()
        return list
    }
}