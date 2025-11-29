package com.duoc.veterinaria.model

import java.time.LocalDate
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class Veterinaria {

    fun ejecutar() {
        println("=".repeat(60))
        println("SISTEMA DE VETERINARIA - FLUJO INTERACTIVO")
        println("=".repeat(60))

        val agenda = Agenda(crearVeterinario(), mutableListOf())
        val resumenConsultas = mutableListOf<String>()

        do {
            val dueno = registrarDueno()
            val mascota = registrarMascota()
            val consulta = registrarConsultaInteractiva()
            agenda.citas.add(consulta)

            val medicamento = seleccionarMedicamento()
            aplicarPromocion(medicamento)
            mostrarReflection(medicamento)
            compararObjetos(dueno, medicamento)

            resumenConsultas.add("""
                Dueño: ${dueno.nombre}, ${dueno.email}, ${dueno.telefono}
                Mascota: ${mascota.nombre}, ${mascota.especie}, ${mascota.edad} años
                Consulta: ${consulta.descripcion}, costo: $${consulta.costoConsulta}
                Medicamento: ${medicamento.nombre}, precio con descuento: $${medicamento.calcularPrecioConDescuento()}
            """.trimIndent())

            print("\n¿Deseas registrar otra atención? (s/n): ")
        } while (readlnOrNull()?.lowercase() == "s")

        mostrarResumenFinal(agenda, resumenConsultas)
    }

    private fun registrarDueno(): Cliente {
        println("\n--- REGISTRO DE DUEÑO ---")

        var nombre: String
        do {
            print("Nombre: ")
            nombre = readlnOrNull()?.trim().orEmpty()
            if (nombre.isEmpty()) println("✗ Nombre no puede estar vacío")
        } while (nombre.isEmpty())

        var email: String
        do {
            print("Email: ")
            email = readlnOrNull()?.trim().orEmpty()
            if (!Validaciones.validarEmail(email)) {
                println("✗ Email inválido. Debe tener formato nombre@dominio.com")
            }
        } while (!Validaciones.validarEmail(email))

        var telefono: String
        var telefonoFormateado: String
        do {
            print("Teléfono (9 dígitos, comienza con 9): ")
            telefono = readlnOrNull()?.trim().orEmpty()
            val digitos = telefono.replace(Regex("[^0-9]"), "")
            val valido = digitos.length == 9 && digitos.startsWith("9")
            if (!valido) println("✗ Teléfono inválido. Debe tener 9 dígitos y comenzar con 9")
        } while (telefono.replace(Regex("[^0-9]"), "").length != 9 || !telefono.startsWith("9"))
        telefonoFormateado = Validaciones.formatearTelefono(telefono)

        println("✓ Dueño registrado: $nombre, $email, $telefonoFormateado")
        return Cliente(nombre, email, telefonoFormateado)
    }

    private fun registrarMascota(): Mascota {
        println("\n--- REGISTRO DE MASCOTA ---")

        var nombre: String
        do {
            print("Nombre: ")
            nombre = readlnOrNull()?.trim().orEmpty()
            if (nombre.isEmpty()) println("✗ Nombre no puede estar vacío")
        } while (nombre.isEmpty())

        var especie: String
        do {
            print("Especie: ")
            especie = readlnOrNull()?.trim().orEmpty()
            if (especie.isEmpty()) println("✗ Especie no puede estar vacía")
        } while (especie.isEmpty())

        var edad: Int
        do {
            print("Edad (0–50): ")
            edad = readlnOrNull()?.toIntOrNull() ?: -1
            if (edad !in 0..50) println("✗ Edad inválida. Debe estar entre 0 y 50")
        } while (edad !in 0..50)

        var peso: Double
        do {
            print("Peso (kg, 0.1–200): ")
            peso = readlnOrNull()?.toDoubleOrNull() ?: -1.0
            if (peso !in 0.1..200.0) println("✗ Peso inválido. Debe estar entre 0.1 y 200 kg")
        } while (peso !in 0.1..200.0)

        println("✓ Mascota registrada: $nombre, $especie, $edad años, $peso kg")
        return Mascota(nombre, especie, edad, peso)
    }

    private fun registrarConsultaInteractiva(): Consulta {
        var cantidad: Int
        do {
            print("Ingrese cantidad de productos (1-100): ")
            cantidad = readlnOrNull()?.toIntOrNull() ?: 0
            if (!Validaciones.validarCantidad(cantidad)) {
                println("✗ Cantidad inválida. Debe estar entre 1 y 100")
            }
        } while (!Validaciones.validarCantidad(cantidad))
        println("✓ Cantidad válida: $cantidad")

        println("\n--- REGISTRO DE CONSULTA ---")
        println("Tipos de atención disponibles:")
        val tipos = listOf("Consulta general", "Urgencia", "Vacunación", "Control")
        tipos.forEachIndexed { i, tipo -> println("${i + 1}. $tipo") }

        var opcion: Int
        do {
            print("Selecciona tipo de atención (1-${tipos.size}): ")
            opcion = readlnOrNull()?.toIntOrNull() ?: -1
            if (opcion !in 1..tipos.size) println("✗ Opción inválida")
        } while (opcion !in 1..tipos.size)

        val tipoSeleccionado = tipos[opcion - 1]
        val costo = when (tipoSeleccionado) {
            "Urgencia" -> 20000.0
            "Vacunación" -> 10000.0
            "Control" -> 12000.0
            else -> 15000.0
        }
        println("✓ Consulta registrada: $tipoSeleccionado, costo: $$costo")
        return Consulta(agendaId(), tipoSeleccionado, costo, "Agendada")
    }

    private fun seleccionarMedicamento(): Medicamento {
        println("\n--- SELECCIÓN DE MEDICAMENTO ---")
        val opciones = listOf(
            Medicamento("Vacuna Rabia", 8000.0, 30),
            Medicamento("Antiparasitario", 15000.0, 50),
            Medicamento("Antiinflamatorio", 9000.0, 20)
        )
        opciones.forEachIndexed { i, med -> println("${i + 1}. ${med.nombre} - $${med.precio}") }

        var opcion: Int
        do {
            print("Selecciona medicamento (1-${opciones.size}): ")
            opcion = readlnOrNull()?.toIntOrNull() ?: -1
            if (opcion !in 1..opciones.size) println("✗ Opción inválida")
        } while (opcion !in 1..opciones.size)

        return opciones[opcion - 1]
    }

    private fun agendaId(): Int = (1000..9999).random()

    private fun crearVeterinario(): Veterinario {
        println("\n--- CREACIÓN DE VETERINARIO ---")
        val disponibilidad = listOf("Lunes 10:00", "Martes 15:00")
        val vet = Veterinario("Dra. López", "Medicina general", disponibilidad)
        println("✓ Veterinario creado: ${vet.nombre}, disponibilidad: ${vet.disponibilidad.joinToString()}")
        return vet
    }

    private fun aplicarPromocion(med: Medicamento) {
        val diaHoy = LocalDate.now().dayOfMonth
        if (Validaciones.estaEnPeriodoPromocional(diaHoy)) {
            println("✓ Fecha dentro del periodo promocional (${diaHoy}): se aplica descuento")
            println("  - Precio con descuento: $${med.calcularPrecioConDescuento()}")
        } else {
            println("✗ Fuera del periodo promocional (${diaHoy}): precio normal $${med.precio}")
        }

        println("\n--- PROMOCIÓN DE MEDICAMENTO ---")
        val anotacion = med::class.findAnnotation<Promocionable>()
        if (anotacion != null) {
            println("✓ Medicamento promocionable: ${med.nombre}")
            println("  - Descuento: ${anotacion.descuento * 100}%")
            println("  - Precio con descuento: $${med.calcularPrecioConDescuento()}")
        }
    }

    private fun mostrarReflection(med: Medicamento) {
        mostrarReflectionCompleta(med)

        println("\n--- REFLECTION SOBRE MEDICAMENTO ---")
        Medicamento::class.memberProperties.forEach {
            println("  - Propiedad: ${it.name}")
        }
    }

    fun mostrarReflectionCompleta(obj: Any) {
        println("Propiedades de ${obj::class.simpleName}:")
        obj::class.memberProperties.forEach { println("  - ${it.name}") }

        println("Métodos de ${obj::class.simpleName}:")
        obj::class.members.filter { it.parameters.size == 1 }.forEach { println("  - ${it.name}") }
    }

    private fun compararObjetos(dueno: Cliente, med: Medicamento) {
        println("\n--- COMPARACIÓN DE OBJETOS ---")
        val otroDueno = Cliente(dueno.nombre, dueno.email, dueno.telefono)
        println("✓ ¿Dueños iguales? ${dueno == otroDueno}")
        val med2 = Medicamento(med.nombre, med.precio, med.stock)
        println("✓ ¿Medicamentos iguales? ${med == med2}")
    }

    private fun mostrarResumenFinal(agenda: Agenda, resumenConsultas: List<String>) {
        println("\n" + "=".repeat(60))
        println("RESUMEN FINAL DE TODAS LAS ATENCIONES")
        println("=".repeat(60))

        resumenConsultas.forEachIndexed { i, resumen ->
            println("\nAtención ${i + 1}:\n$resumen")
        }

        println("\n✓ Veterinario: ${agenda.veterinario.nombre}, citas registradas: ${agenda.citas.size}")

        // DEMOSTRACIÓN DE DESESTRUCTURACIÓN
        println("\n" + "=".repeat(60))
        println("DEMOSTRACIÓN DE DESESTRUCTURACIÓN")
        println("=".repeat(60))

        // Crear un cliente de ejemplo para desestructurar
        val clienteEjemplo = Cliente("Juan Pérez", "juan@email.com", "+56 9 1234 5678")
        val (nombreCliente, emailCliente, telefonoCliente) = clienteEjemplo
        println("✓ Cliente desestructurado:")
        println("  - Nombre: $nombreCliente")
        println("  - Email: $emailCliente")
        println("  - Teléfono: $telefonoCliente")

        // Crear medicamentos de ejemplo
        val med1 = Medicamento("Vacuna Rabia", 8000.0, 30)
        val med2 = Medicamento("Antiparasitario", 15000.0, 50)

        // Crear pedidos de ejemplo
        val pedido1 = Pedido(1001, clienteEjemplo, listOf(med1), med1.precio)
        val pedido2 = Pedido(1002, clienteEjemplo, listOf(med2), med2.precio)

        // Desestructurar pedido
        val (idPedido, clientePedido, medicamentos, totalPedido) = pedido1
        println("\n✓ Pedido desestructurado:")
        println("  - ID: $idPedido")
        println("  - Cliente: ${clientePedido.nombre}")
        println("  - Medicamentos: ${medicamentos.size}")
        println("  - Total: $$totalPedido")

        // DEMOSTRACIÓN DE COMBINACIÓN DE PEDIDOS
        println("\n" + "=".repeat(60))
        println("DEMOSTRACIÓN DE COMBINACIÓN DE PEDIDOS (Operador +)")
        println("=".repeat(60))

        println("✓ Pedido 1: ${pedido1.medicamentos.size} medicamento(s), total: $${pedido1.total}")
        println("✓ Pedido 2: ${pedido2.medicamentos.size} medicamento(s), total: $${pedido2.total}")

        val pedidoCombinado = pedido1 + pedido2
        println("✓ Pedido Combinado: ${pedidoCombinado.medicamentos.size} medicamentos, total: $${pedidoCombinado.total}")
        println("  - Medicamentos incluidos:")
        pedidoCombinado.medicamentos.forEach { med ->
            println("    • ${med.nombre}: $${med.precio}")
        }

        // DEMOSTRACIÓN DE PREVENCIÓN DE DUPLICADOS
        println("\n" + "=".repeat(60))
        println("DEMOSTRACIÓN DE PREVENCIÓN DE DUPLICADOS")
        println("=".repeat(60))

        val cliente1 = Cliente("María González", "maria@email.com", "+56 9 8765 4321")
        val cliente2 = Cliente("María González", "maria@email.com", "+56 9 8765 4321")
        val cliente3 = Cliente("Pedro López", "pedro@email.com", "+56 9 5555 5555")

        println("✓ ¿Cliente1 == Cliente2? ${cliente1 == cliente2} (mismo nombre y email)")
        println("✓ ¿Cliente1 == Cliente3? ${cliente1 == cliente3} (diferente nombre y email)")
        println("✓ HashCode Cliente1: ${cliente1.hashCode()}")
        println("✓ HashCode Cliente2: ${cliente2.hashCode()} (igual al Cliente1)")
        println("✓ HashCode Cliente3: ${cliente3.hashCode()} (diferente)")

        // Demostrar prevención de duplicados con Set
        val clientesUnicos = mutableSetOf<Cliente>()
        clientesUnicos.add(cliente1)
        clientesUnicos.add(cliente2) // No se agregará porque es igual a cliente1
        clientesUnicos.add(cliente3)

        println("\n✓ Clientes únicos registrados: ${clientesUnicos.size} (de 3 intentos)")
        println("  - Se previno 1 duplicado exitosamente")

        println("\n" + "=".repeat(60))
    }
}