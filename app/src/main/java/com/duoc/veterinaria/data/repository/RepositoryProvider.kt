package com.duoc.veterinaria.data.repository

/**
 * Proveedor Singleton de repositorios.
 * Garantiza que todos los ViewModels usen la misma instancia.
 *
 * Principio: Dependency Injection manual (en producción usaríamos Hilt/Koin)
 */
object RepositoryProvider {

    // Instancia única compartida del repositorio de atenciones
    val atencionRepository: AtencionRepository by lazy {
        AtencionRepositoryImpl()
    }

    // Instancia única compartida del repositorio de clientes
    val clienteRepository: ClienteRepository by lazy {
        ClienteRepositoryImpl()
    }
}