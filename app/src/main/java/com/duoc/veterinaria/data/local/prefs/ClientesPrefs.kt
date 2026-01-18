package com.duoc.veterinaria.data.local.prefs

import android.content.Context

// Esta clase gestiona las preferencias relacionadas con clientes:
//Guarda el último email usado
//Guarda la marca temporal del último guardado
class ClientesPrefs(context: Context) {

    private val sp = context.getSharedPreferences("clientes_prefs", Context.MODE_PRIVATE)

    fun setUltimoEmail(email: String) {
        sp.edit().putString("ultimo_email", email).apply()
    }

    fun getUltimoEmail(): String = sp.getString("ultimo_email", "") ?: ""

    fun setUltimoGuardadoMillis(value: Long) {
        sp.edit().putLong("ultimo_guardado", value).apply()
    }

    fun getUltimoGuardadoMillis(): Long = sp.getLong("ultimo_guardado", 0L)
}