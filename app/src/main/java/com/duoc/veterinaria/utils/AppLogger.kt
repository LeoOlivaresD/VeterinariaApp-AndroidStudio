package com.duoc.veterinaria.utils

import android.util.Log

/**
 * Sistema centralizado de logging para facilitar depuración.
 * Permite filtrar logs por tags y niveles específicos.
 */
object AppLogger {

    private const val TAG_PREFIX = "VET_APP"

    // Tags específicos por módulo
    object Tags {
        const val DATABASE = "DATABASE"
        const val NETWORK = "NETWORK"
        const val UI = "UI"
        const val VIEWMODEL = "VIEWMODEL"
        const val REPOSITORY = "REPOSITORY"
        const val ERROR = "ERROR"
        const val PERFORMANCE = "PERFORMANCE"
    }

    /**
     * Log de depuración (nivel DEBUG)
     */
    fun d(tag: String, message: String) {
        Log.d("$TAG_PREFIX:$tag", message)
    }

    /**
     * Log informativo (nivel INFO)
     */
    fun i(tag: String, message: String) {
        Log.i("$TAG_PREFIX:$tag", message)
    }

    /**
     * Log de advertencia (nivel WARN)
     */
    fun w(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.w("$TAG_PREFIX:$tag", message, throwable)
        } else {
            Log.w("$TAG_PREFIX:$tag", message)
        }
    }

    /**
     * Log de error (nivel ERROR)
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            Log.e("$TAG_PREFIX:$tag", message, throwable)
        } else {
            Log.e("$TAG_PREFIX:$tag", message)
        }
    }

    /**
     * Log de rendimiento con medición de tiempo
     */
    fun performance(tag: String, operation: String, timeMillis: Long) {
        Log.i("$TAG_PREFIX:${Tags.PERFORMANCE}", "[$tag] $operation took ${timeMillis}ms")
    }

    /**
     * Log de entrada a un método
     */
    fun methodEntry(tag: String, methodName: String, params: String = "") {
        val message = if (params.isNotEmpty()) {
            "Entering $methodName with params: $params"
        } else {
            "Entering $methodName"
        }
        d(tag, message)
    }

    /**
     * Log de salida de un método
     */
    fun methodExit(tag: String, methodName: String, result: String = "") {
        val message = if (result.isNotEmpty()) {
            "Exiting $methodName with result: $result"
        } else {
            "Exiting $methodName"
        }
        d(tag, message)
    }
}