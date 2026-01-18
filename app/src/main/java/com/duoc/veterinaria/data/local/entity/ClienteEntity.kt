package com.duoc.veterinaria.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa la tabla "clientes" en la base de datos local.
 *
 * Esta clase define la estructura de los datos que serán almacenados
 * de forma persistente en SQLite a través de Room.
 */
@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val email: String,
    val telefono: String
)