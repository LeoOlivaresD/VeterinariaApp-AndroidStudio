package com.duoc.veterinaria.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duoc.veterinaria.data.local.entity.ClienteEntity
import kotlinx.coroutines.flow.Flow
/*
* Aca definimos 3 operaciones
getAll(): obtiene todos los clientes como un Flow (observable)
insert(): inserta un nuevo cliente
deleteAll(): elimina todos los clientes (útil para testing)
* */
@Dao
interface ClienteDao {

    @Query("SELECT * FROM clientes ORDER BY id DESC")
    fun getAll(): Flow<List<ClienteEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(cliente: ClienteEntity): Long

    @Query("DELETE FROM clientes")
    suspend fun deleteAll()
}