package com.duoc.veterinaria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.duoc.veterinaria.ui.theme.VeterinariaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VeterinariaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VeterinariaApp()
                }
            }
        }
    }
}

@Composable
fun VeterinariaApp() {
    var currentScreen by remember { mutableStateOf("welcome") }
    var registros by remember { mutableStateOf(listOf<RegistroAtencion>()) }

    when (currentScreen) {
        "welcome" -> WelcomeScreen(
            onStartClick = { currentScreen = "registro" }
        )
        "registro" -> RegistroScreen(
            onRegistroComplete = { registro ->
                registros = registros + registro
                currentScreen = "resumen"
            },
            onBackClick = { currentScreen = "welcome" }
        )
        "resumen" -> ResumenScreen(
            registros = registros,
            onNuevaAtencion = { currentScreen = "registro" },
            onVolverInicio = { currentScreen = "welcome" }
        )
    }
}

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
                onComplete = { cons ->
                    consulta = cons
                    step = 4
                },
                onBack = { step = 2 }
            )
            4 -> SeleccionMedicamentoForm(
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
                    "Email inválido (ejemplo: usuario@dominio.com)"
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
                    "Teléfono debe tener 9 dígitos y comenzar con 9"
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
            label = { Text("Especie (ej: Perro, Gato)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it.filter { char -> char.isDigit() } },
            label = { Text("Edad (0-50 años)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg, 0.1-200)") },
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
                enabled = nombre.isNotEmpty() &&
                        especie.isNotEmpty() &&
                        (edad.toIntOrNull() ?: -1) in 0..50 &&
                        (peso.toDoubleOrNull() ?: -1.0) in 0.1..200.0
            ) {
                Text("Siguiente")
            }
        }
    }
}

@Composable
fun RegistroConsultaForm(
    onComplete: (Consulta) -> Unit,
    onBack: () -> Unit
) {
    var tipoSeleccionado by remember { mutableStateOf("") }
    val tipos = listOf(
        "Consulta general" to 15000.0,
        "Urgencia" to 20000.0,
        "Vacunación" to 10000.0,
        "Control" to 12000.0
    )

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
            OutlinedButton(onClick = onBack) {
                Text("Volver")
            }
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
    onComplete: (Medicamento) -> Unit,
    onBack: () -> Unit
) {
    var medicamentoSeleccionado by remember { mutableStateOf<Medicamento?>(null) }
    val medicamentos = listOf(
        Medicamento("Vacuna Rabia", 8000.0, 30),
        Medicamento("Antiparasitario", 15000.0, 50),
        Medicamento("Antiinflamatorio", 9000.0, 20)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Seleccionar Medicamento", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))

        medicamentos.forEach { med ->
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = medicamentoSeleccionado == med,
                        onClick = { medicamentoSeleccionado = med }
                    )
                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text(med.nombre, fontWeight = FontWeight.Medium)
                        Text("Precio: $${med.precio}", fontSize = 14.sp)
                        Text("Precio con descuento: $${med.calcularPrecioConDescuento()}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary)
                        Text("Stock: ${med.stock}", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack) {
                Text("Volver")
            }
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Resumen de Atenciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Total de atenciones: ${registros.size}",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        registros.forEachIndexed { index, registro ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Atención ${index + 1}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Dueño: ${registro.dueno.nombre}")
                    Text("Email: ${registro.dueno.email}")
                    Text("Teléfono: ${registro.dueno.telefono}")

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Mascota: ${registro.mascota.nombre}")
                    Text("Especie: ${registro.mascota.especie}")
                    Text("Edad: ${registro.mascota.edad} años")
                    Text("Peso: ${registro.mascota.peso} kg")

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Consulta: ${registro.consulta.descripcion}")
                    Text("Costo: $${registro.consulta.costoConsulta}")

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Medicamento: ${registro.medicamento.nombre}")
                    Text("Precio con descuento: $${registro.medicamento.calcularPrecioConDescuento()}")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNuevaAtencion,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Registrar Nueva Atención", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onVolverInicio,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Volver al Inicio", fontSize = 16.sp)
        }
    }
}

data class RegistroAtencion(
    val dueno: Cliente,
    val mascota: Mascota,
    val consulta: Consulta,
    val medicamento: Medicamento
)