package com.duoc.veterinaria.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duoc.veterinaria.data.local.dao.ClienteDao
import com.duoc.veterinaria.data.local.entity.ClienteEntity

// En esta clase implementamos el patrón Singleton para garantizar una única instancia
// de la base de datos durante toda la ejecución de la app.
@Database(entities = [ClienteEntity::class], version = 1, exportSchema = false)
abstract class VeterinariaDatabase : RoomDatabase() {

    abstract fun clienteDao(): ClienteDao

    companion object {
        @Volatile
        private var INSTANCE: VeterinariaDatabase? = null

        fun getInstance(context: Context): VeterinariaDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    VeterinariaDatabase::class.java,
                    "veterinaria.db"
                ).build().also { INSTANCE = it }
            }
    }
}