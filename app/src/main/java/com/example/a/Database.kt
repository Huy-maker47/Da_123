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
        val qry1 = "CREATE TABLE users(username TEXT, email TEXT, password TEXT)"
        db.execSQL(qry1)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
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
        val str = arrayOf(username, password)

        val db = readableDatabase
        val c = db.rawQuery(
            "SELECT * FROM users WHERE username = ? AND password = ?",
            str
        )

        if (c.moveToFirst()) {
            result = 1
        }

        c.close()
        db.close()
        return result
    }

}