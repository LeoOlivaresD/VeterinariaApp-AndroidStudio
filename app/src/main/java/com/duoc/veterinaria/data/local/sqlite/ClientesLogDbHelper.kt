package com.duoc.veterinaria.data.local.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//Esta clase usa SQLite tradicional para registrar eventos como
// inserciones de clientes, con timestamp automático.

class ClientesLogDbHelper(context: Context) :
    SQLiteOpenHelper(context, "veterinaria_logs.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE clientes_log(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                evento TEXT NOT NULL,
                fechaMillis INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS clientes_log")
        onCreate(db)
    }

    fun addLog(evento: String) {
        val values = ContentValues().apply {
            put("evento", evento)
            put("fechaMillis", System.currentTimeMillis())
        }
        writableDatabase.insert("clientes_log", null, values)
    }
}