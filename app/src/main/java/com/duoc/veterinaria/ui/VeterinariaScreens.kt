package com.duoc.veterinaria.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.veterinaria.data.model.*
import com.duoc.veterinaria.data.service.VeterinariaService
import com.duoc.veterinaria.utils.Validaciones
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// -----------------------------------------------------------------------
// 1. COMPONENTE DE MENÚ REUTILIZABLE
// -----------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariaTopBar(
    titulo: String,
    onNavigateTo: (String) -> Unit,
    onExit: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menú")
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Inicio") },
                    onClick = { menuExpanded = false; onNavigateTo("welcome") }
                )
                DropdownMenuItem(
                    text = { Text("Registrar Atención") },
                    onClick = { menuExpanded = false; onNavigateTo("registro") }
                )
                DropdownMenuItem(
                    text = { Text("Ver Historial") },
                    onClick = { menuExpanded = false; onNavigateTo("resumen") }
                )
                Divider() // CORREGIDO: Usamos Divider en lugar de HorizontalDivider
                DropdownMenuItem(
                    text = { Text("Salir", color = Color.Red) },
                    onClick = { menuExpanded = false; onExit() }
                )
            }
        }
    )
}

// -----------------------------------------------------------------------
// PANTALLAS
// -----------------------------------------------------------------------

@Composable
fun WelcomeScreen(
    onStartClick: () -> Unit,
    onVerRegistrosClick: () -> Unit,
    onFinalizarApp: () -> Unit,
    totalMascotas: Int,
    totalConsultas: Int,
    ultimoDueno: String,
    onNavigateTo: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { VeterinariaTopBar("Veterinaria Duoc", onNavigateTo, onFinalizarApp) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

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
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("📊 Resumen del Sistema", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("🐾 Mascotas atendidas: $totalMascotas")
                        Text("📝 Consultas totales: $totalConsultas")
                        Text("👤 Último dueño: $ultimoDueno")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            delay(1500)
                            isLoading = false
                            onStartClick()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !isLoading
                ) {
                    Text("Registrar Nueva Atención")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            delay(1000)
                            isLoading = false
                            onVerRegistrosClick()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !isLoading
                ) {
                    Text("Ver Consultas Registradas")
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun RegistroScreen(
    service: VeterinariaService,
    onRegistroComplete: (RegistroAtencion) -> Unit,
    onBackClick: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onExit: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var dueno by remember { mutableStateOf<Cliente?>(null) }
    var mascota by remember { mutableStateOf<Mascota?>(null) }
    var consulta by remember { mutableStateOf<Consulta?>(null) }

    var listaMedicamentosAMostrar by remember { mutableStateOf<List<Medicamento>>(emptyList()) }
    var tituloPantallaMedicamentos by remember { mutableStateOf("Seleccionar Producto") }

    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { VeterinariaTopBar("Registro de Atención", onNavigateTo, onExit) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                LinearProgressIndicator(
                    progress = step / 4f, // CORREGIDO: Sin llaves {}
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                )

                Text(
                    text = "Paso $step de 4",
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp)
                )

                when (step) {
                    1 -> RegistroDuenoForm(onComplete = { dueno = it; step = 2 }, onBack = onBackClick)
                    2 -> RegistroMascotaForm(onComplete = { mascota = it; step = 3 }, onBack = { step = 1 })
                    3 -> RegistroConsultaForm(
                        service = service,
                        onComplete = { cons ->
                            consulta = cons
                            when (cons.descripcion) {
                                "Urgencia" -> {
                                    listaMedicamentosAMostrar = service.obtenerMedicamentosGenerales()
                                    tituloPantallaMedicamentos = "Seleccionar Medicamento"
                                    step = 4
                                }
                                "Vacunación" -> {
                                    listaMedicamentosAMostrar = service.obtenerVacunas()
                                    tituloPantallaMedicamentos = "Seleccionar Vacuna"
                                    step = 4
                                }
                                else -> {
                                    val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                                    val registro = RegistroAtencion(
                                        dueno = dueno!!, mascota = mascota!!, consulta = cons,
                                        medicamento = Medicamento("N/A", 0.0, 0),
                                        veterinario = service.obtenerNombreVeterinario(),
                                        fecha = fechaHoy, precioFinalMedicamento = 0.0, detalleDescuento = ""
                                    )
                                    scope.launch {
                                        isSaving = true
                                        delay(1500)
                                        isSaving = false
                                        onRegistroComplete(registro)
                                    }
                                }
                            }
                        },
                        onBack = { step = 2 }
                    )
                    4 -> SeleccionMedicamentoForm(
                        service = service,
                        listaMedicamentos = listaMedicamentosAMostrar,
                        titulo = tituloPantallaMedicamentos,
                        onComplete = { med ->
                            val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                            val registro = RegistroAtencion(
                                dueno = dueno!!, mascota = mascota!!, consulta = consulta!!,
                                medicamento = med,
                                veterinario = service.obtenerNombreVeterinario(),
                                fecha = fechaHoy,
                                precioFinalMedicamento = service.obtenerPrecioFinalMedicamento(med),
                                detalleDescuento = service.obtenerDetalleDescuento(med)
                            )
                            scope.launch {
                                isSaving = true
                                delay(1500)
                                isSaving = false
                                onRegistroComplete(registro)
                            }
                        },
                        onBack = { step = 3 }
                    )
                }
            }

            if (isSaving) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Guardando registro...", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegistroDuenoForm(onComplete: (Cliente) -> Unit, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var errorNombre by remember { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var errorTelefono by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it; errorNombre = if (it.isEmpty()) "Requerido" else "" },
            label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), isError = errorNombre.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorEmail = if (!Validaciones.validarEmail(it)) "Email inválido" else "" },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), isError = errorEmail.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it
                val d = it.replace(Regex("[^0-9]"), "")
                errorTelefono = if (d.length != 9 || !d.startsWith("9")) "Debe ser 9 dígitos (9xxxxxxxx)" else ""
            },
            label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth(), isError = errorTelefono.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack) { Text("Volver") }
            Button(
                onClick = { onComplete(Cliente(nombre, email, Validaciones.formatearTelefono(telefono))) },
                enabled = nombre.isNotEmpty() && Validaciones.validarEmail(email) && telefono.replace(Regex("[^0-9]"), "").length == 9
            ) { Text("Siguiente") }
        }
    }
}

@Composable
fun RegistroMascotaForm(onComplete: (Mascota) -> Unit, onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = especie, onValueChange = { especie = it }, label = { Text("Especie") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = edad, onValueChange = { edad = it.filter { c -> c.isDigit() } }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = peso, onValueChange = { peso = it }, label = { Text("Peso") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack) { Text("Volver") }
            Button(
                onClick = { onComplete(Mascota(nombre, especie, edad.toIntOrNull() ?: 0, peso.toDoubleOrNull() ?: 0.0)) },
                enabled = nombre.isNotEmpty() && especie.isNotEmpty()
            ) { Text("Siguiente") }
        }
    }
}

// CORREGIDO: Agregamos la anotación @OptIn para permitir el uso de onClick en Card
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroConsultaForm(service: VeterinariaService, onComplete: (Consulta) -> Unit, onBack: () -> Unit) {
    var selected by remember { mutableStateOf("") }
    val tipos = remember { service.obtenerTiposAtencion() }

    Column {
        tipos.forEach { (tipo, costo) ->
            Card(
                onClick = { selected = tipo },
                colors = CardDefaults.cardColors(containerColor = if(selected==tipo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(Modifier.padding(16.dp)) {
                    RadioButton(selected == tipo, onClick = { selected = tipo })
                    Text("$tipo - $$costo", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack) { Text("Volver") }
            Button(
                onClick = {
                    val costo = tipos.find { it.first == selected }?.second ?: 0.0
                    onComplete(Consulta((1000..9999).random(), selected, costo, "Agendada"))
                },
                enabled = selected.isNotEmpty()
            ) { Text("Siguiente") }
        }
    }
}

// CORREGIDO: Agregamos la anotación @OptIn para permitir el uso de onClick en Card
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionMedicamentoForm(service: VeterinariaService, listaMedicamentos: List<Medicamento>, titulo: String, onComplete: (Medicamento) -> Unit, onBack: () -> Unit) {
    var selected by remember { mutableStateOf<Medicamento?>(null) }
    Column {
        Text(titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        listaMedicamentos.forEach { med ->
            val final = service.obtenerPrecioFinalMedicamento(med)
            Card(
                onClick = { selected = med },
                colors = CardDefaults.cardColors(containerColor = if(selected==med) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected == med, onClick = { selected = med })
                        Text(med.nombre, fontWeight = FontWeight.Bold)
                    }
                    Text("Precio: $$final", modifier = Modifier.padding(start = 32.dp))
                    if (final < med.precio) Text(service.obtenerDetalleDescuento(med), color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(start = 32.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack) { Text("Volver") }
            Button(onClick = { selected?.let { onComplete(it) } }, enabled = selected != null) { Text("Finalizar") }
        }
    }
}

@Composable
fun ResumenScreen(
    registros: List<RegistroAtencion>,
    onNuevaAtencion: () -> Unit,
    onVolverInicio: () -> Unit,
    onNavigateTo: (String) -> Unit,
    onExit: () -> Unit
) {
    Scaffold(
        topBar = { VeterinariaTopBar("Historial de Atenciones", onNavigateTo, onExit) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (registros.isEmpty()) {
                Text("No hay atenciones registradas.", modifier = Modifier.padding(vertical = 16.dp))
            } else {
                registros.forEachIndexed { index, registro ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Atención ${index + 1} - ${registro.fecha}", fontWeight = FontWeight.Bold)
                            Text("Dueño: ${registro.dueno.nombre}")
                            Text("Mascota: ${registro.mascota.nombre}")
                            Text("Consulta: ${registro.consulta.descripcion}")
                            if(registro.medicamento.nombre != "N/A") {
                                Text("Medicamento: ${registro.medicamento.nombre} ($${registro.precioFinalMedicamento})")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onNuevaAtencion, modifier = Modifier.fillMaxWidth()) { Text("Registrar otra atención") }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onVolverInicio, modifier = Modifier.fillMaxWidth()) { Text("Volver al Inicio") }
        }
    }
}