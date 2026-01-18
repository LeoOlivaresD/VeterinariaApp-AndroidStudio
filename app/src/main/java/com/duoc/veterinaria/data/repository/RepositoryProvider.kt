package com.duoc.veterinaria.data.repository

/**
 * Proveedor Singleton de repositorios.
 * Garantiza que todos los ViewModels usen la misma instancia.
 *
 * Principio: Dependency Injection manual (en producción usaríamos Hilt/Koin)
 */
object RepositoryProvider {

    val atencionRepository: AtencionRepository by lazy {
        AtencionRepositoryImpl()
    }

    val clienteRepository: ClienteRepository by lazy<ClienteRepository> {
        ClienteRepositoryImpl()
    }
}