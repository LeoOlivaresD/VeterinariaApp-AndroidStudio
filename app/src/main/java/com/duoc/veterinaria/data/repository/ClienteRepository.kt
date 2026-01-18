package com.duoc.veterinaria.data.repository

import com.duoc.veterinaria.data.local.dao.ClienteDao
import com.duoc.veterinaria.data.local.entity.ClienteEntity
import com.duoc.veterinaria.data.local.prefs.ClientesPrefs
import com.duoc.veterinaria.data.local.sqlite.ClientesLogDbHelper
import kotlinx.coroutines.flow.Flow

//Este repositorio integra las tres tecnologías de persistencia en un solo lugar.
class ClienteRepository(
    private val dao: ClienteDao,
    private val prefs: ClientesPrefs,
    private val logs: ClientesLogDbHelper
) {
    fun clientesFlow(): Flow<List<ClienteEntity>> = dao.getAll()

    suspend fun registrarCliente(nombre: String, email: String, telefono: String): Boolean {
        val id = dao.insert(ClienteEntity(nombre = nombre, email = email, telefono = telefono))

        // SQLite: registrar en bitácora
        logs.addLog("Insert cliente id=$id email=$email")

        // SharedPreferences: guardar último email
        prefs.setUltimoEmail(email)
        prefs.setUltimoGuardadoMillis(System.currentTimeMillis())

        return id > 0
    }
}