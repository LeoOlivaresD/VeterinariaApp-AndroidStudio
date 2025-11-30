package com.duoc.veterinaria

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.veterinaria.model.*
import com.duoc.veterinaria.service.VeterinariaService
import com.duoc.veterinaria.utils.Validaciones

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenidos al servicio de atención veterinaria",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Registrar Nueva Atención", fontSize = 18.sp)
        }
    }
}

@Composable
fun RegistroScreen(
    service: VeterinariaService,
    onRegistroComplete: (RegistroAtencion) -> Unit,
    onBackClick: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var dueno by remember { mutableStateOf<Cliente?>(null) }
    var mascota by remember { mutableStateOf<Mascota?>(null) }
    var consulta by remember { mutableStateOf<Consulta?>(null) }
    var medicamento by remember { mutableStateOf<Medicamento?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Registro de Atención - Paso $step de 4",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when (step) {
            1 -> RegistroDuenoForm(
                onComplete = { cliente ->
                    dueno = cliente
                    step = 2
                },
                onBack = onBackClick
            )
            2 -> RegistroMascotaForm(
                onComplete = { pet ->
                    mascota = pet
                    step = 3
                },
                onBack = { step = 1 }
            )
            3 -> RegistroConsultaForm(
                service = service,
                onComplete = { cons ->
                    consulta = cons
                    step = 4
                },
                onBack = { step = 2 }
            )
            4 -> SeleccionMedicamentoForm(
                service = service,
                onComplete = { med ->
                    medicamento = med
                    val registro = RegistroAtencion(
                        dueno = dueno!!,
                        mascota = mascota!!,
                        consulta = consulta!!,
                        medicamento = med
                    )
                    onRegistroComplete(registro)
                },
                onBack = { step = 3 }
            )
        }
    }
}

@Composable
fun RegistroDuenoForm(
    onComplete: (Cliente) -> Unit,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var errorTelefono by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Datos del Dueño", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = if (it.isEmpty()) "Nombre requerido" else ""
            },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorNombre.isNotEmpty()
        )
        if (errorNombre.isNotEmpty()) {
            Text(errorNombre, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorEmail = if (!Validaciones.validarEmail(it)) {
                    "Email inválido"
                } else ""
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorEmail.isNotEmpty()
        )
        if (errorEmail.isNotEmpty()) {
            Text(errorEmail, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it
                val digitos = it.replace(Regex("[^0-9]"), "")
                errorTelefono = if (digitos.length != 9 || !digitos.startsWith("9")) {
                    "Debe tener 9 dígitos y empezar con 9"
                } else ""
            },
            label = { Text("Teléfono (ej: 912345678)") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorTelefono.isNotEmpty()
        )
        if (errorTelefono.isNotEmpty()) {
            Text(errorTelefono, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) {
                Text("Volver")
            }
            Button(
                onClick = {
                    val telefonoFormateado = Validaciones.formatearTelefono(telefono)
                    onComplete(Cliente(nombre, email, telefonoFormateado))
                },
                enabled = nombre.isNotEmpty() &&
                        Validaciones.validarEmail(email) &&
                        telefono.replace(Regex("[^0-9]"), "").let {
                            it.length == 9 && it.startsWith("9")
                        }
            ) {
                Text("Siguiente")
            }
        }
    }
}

@Composable
fun RegistroMascotaForm(
    onComplete: (Mascota) -> Unit,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Datos de la Mascota", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = especie,
            onValueChange = { especie = it },
            label = { Text("Especie") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it.filter { char -> char.isDigit() } },
            label = { Text("Edad (0-50)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) {
                Text("Volver")
            }
            Button(
                onClick = {
                    val edadInt = edad.toIntOrNull() ?: 0
                    val pesoDouble = peso.toDoubleOrNull() ?: 0.0
                    onComplete(Mascota(nombre, especie, edadInt, pesoDouble))
                },
                enabled = nombre.isNotEmpty() && especie.isNotEmpty()
            ) {
                Text("Siguiente")
            }
        }
    }
}

@Composable
fun RegistroConsultaForm(
    service: VeterinariaService,
    onComplete: (Consulta) -> Unit,
    onBack: () -> Unit
) {
    var tipoSeleccionado by remember { mutableStateOf("") }
    // Usamos el Service para obtener la lista
    val tipos = remember { service.obtenerTiposAtencion() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Tipo de Consulta", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))

        tipos.forEach { (tipo, costo) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (tipoSeleccionado == tipo)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tipoSeleccionado == tipo,
                        onClick = { tipoSeleccionado = tipo }
                    )
                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text(tipo, fontWeight = FontWeight.Medium)
                        Text("$$costo", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) { Text("Volver") }
            Button(
                onClick = {
                    val costo = tipos.find { it.first == tipoSeleccionado }?.second ?: 0.0
                    onComplete(Consulta((1000..9999).random(), tipoSeleccionado, costo, "Agendada"))
                },
                enabled = tipoSeleccionado.isNotEmpty()
            ) {
                Text("Siguiente")
            }
        }
    }
}

@Composable
fun SeleccionMedicamentoForm(
    service: VeterinariaService,
    onComplete: (Medicamento) -> Unit,
    onBack: () -> Unit
) {
    var medicamentoSeleccionado by remember { mutableStateOf<Medicamento?>(null) }
    // Usamos el Service
    val medicamentos = remember { service.obtenerMedicamentosDisponibles() }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Seleccionar Medicamento", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))

        medicamentos.forEach { med ->
            val precioFinal = service.obtenerPrecioFinalMedicamento(med)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (medicamentoSeleccionado == med)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = medicamentoSeleccionado == med,
                        onClick = { medicamentoSeleccionado = med }
                    )
                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text(med.nombre, fontWeight = FontWeight.Medium)
                        Text("Precio lista: $${med.precio}", fontSize = 12.sp)
                        Text("Precio final: $$precioFinal", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) { Text("Volver") }
            Button(
                onClick = { medicamentoSeleccionado?.let { onComplete(it) } },
                enabled = medicamentoSeleccionado != null
            ) {
                Text("Finalizar")
            }
        }
    }
}

@Composable
fun ResumenScreen(
    registros: List<RegistroAtencion>,
    onNuevaAtencion: () -> Unit,
    onVolverInicio: () -> Unit
) {
    // La pantalla de resumen queda igual que en tu código original
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Resumen de Atenciones", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        registros.forEachIndexed { index, registro ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Atención ${index + 1}", fontWeight = FontWeight.Bold)
                    Text("Dueño: ${registro.dueno.nombre}")
                    Text("Mascota: ${registro.mascota.nombre} (${registro.mascota.especie})")
                    Text("Consulta: ${registro.consulta.descripcion} - $${registro.consulta.costoConsulta}")
                    Text("Medicamento: ${registro.medicamento.nombre}")
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNuevaAtencion, modifier = Modifier.fillMaxWidth()) {
            Text("Nueva Atención")
        }
        OutlinedButton(onClick = onVolverInicio, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al Inicio")
        }
    }
}