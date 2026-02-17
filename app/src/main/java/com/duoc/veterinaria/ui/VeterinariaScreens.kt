package com.duoc.veterinaria.ui

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.veterinaria.viewmodel.ClientesViewModel
import com.duoc.veterinaria.viewmodel.ClientesViewModelFactory
import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.duoc.veterinaria.R
import com.duoc.veterinaria.data.model.*
import com.duoc.veterinaria.ui.navigation.AppScreen
import com.duoc.veterinaria.ui.navigation.VeterinariaTopBar
import com.duoc.veterinaria.utils.Validaciones
import com.duoc.veterinaria.viewmodel.RegistroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    onNavigateTo: (AppScreen) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val navegacionConCarga: (AppScreen) -> Unit = { screen ->
        scope.launch {
            isLoading = true
            delay(1000)
            isLoading = false
            onNavigateTo(screen)
        }
    }

    Scaffold(
        topBar = { VeterinariaTopBar("Veterinaria Duoc", navegacionConCarga, onFinalizarApp) }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            // --- IMAGEN DE FONDO ---
            Image(
                painter = painterResource(id = R.drawable.fondo_home),
                contentDescription = "Fondo",
                contentScale = ContentScale.Crop,
                alpha = 0.3f,
                modifier = Modifier.fillMaxSize()
            )

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
                            delay(1000)
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
                PantallaDeCarga(mensaje = "Cargando módulos...")
            }
        }
    }
}

@Composable
fun RegistroScreen(
    viewModel: RegistroViewModel,
    onRegistroComplete: () -> Unit,
    onBackClick: () -> Unit,
    onNavigateTo: (AppScreen) -> Unit,
    onExit: () -> Unit
) {
    // Observar estados del ViewModel
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    // Estados locales de la UI
    var step by remember { mutableStateOf(1) }
    var dueno by remember { mutableStateOf<Cliente?>(null) }
    var mascota by remember { mutableStateOf<Mascota?>(null) }
    var consulta by remember { mutableStateOf<Consulta?>(null) }

    var listaMedicamentos by remember { mutableStateOf<List<Medicamento>>(emptyList()) }
    var tituloMedicamentos by remember { mutableStateOf("Seleccionar Producto") }

    Scaffold(
        topBar = {
            VeterinariaTopBar("Registro de Atención", onNavigateTo, onExit)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Indicador de progreso
                LinearProgressIndicator(
                    progress = step / 4f,  // ← SIN llaves
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                )

                Text(
                    text = "Paso $step de 4",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Mostrar error si existe
                errorMessage?.let { error ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                when (step) {
                    1 -> RegistroDuenoForm(
                        onComplete = { cliente ->
                            dueno = cliente
                            step = 2
                        },
                        onBack = onBackClick
                    )

                    2 -> RegistroMascotaForm(
                        onComplete = { m ->
                            mascota = m
                            step = 3
                        },
                        onBack = { step = 1 }
                    )

                    3 -> RegistroConsultaForm(
                        tiposAtencion = viewModel.obtenerTiposAtencion(),
                        onComplete = { cons ->
                            consulta = cons
                            when (cons.descripcion) {
                                "Urgencia" -> {
                                    listaMedicamentos = viewModel.obtenerMedicamentosGenerales()
                                    tituloMedicamentos = "Seleccionar Medicamento"
                                    step = 4
                                }
                                "Vacunación" -> {
                                    listaMedicamentos = viewModel.obtenerVacunas()
                                    tituloMedicamentos = "Seleccionar Vacuna"
                                    step = 4
                                }
                                else -> {
                                    // Registrar sin medicamento
                                    viewModel.registrarAtencionSinMedicamento(
                                        dueno = dueno!!,
                                        mascota = mascota!!,
                                        consulta = cons,
                                        onComplete = onRegistroComplete
                                    )
                                }
                            }
                        },
                        onBack = { step = 2 }
                    )

                    4 -> SeleccionMedicamentoForm(
                        viewModel = viewModel,
                        listaMedicamentos = listaMedicamentos,
                        titulo = tituloMedicamentos,
                        onComplete = { med ->
                            viewModel.registrarAtencion(
                                dueno = dueno!!,
                                mascota = mascota!!,
                                consulta = consulta!!,
                                medicamento = med,
                                onComplete = onRegistroComplete
                            )
                        },
                        onBack = { step = 3 }
                    )
                }
            }

            // Overlay de carga
            if (isLoading) {
                PantallaDeCarga(mensaje = "Guardando registro...")
            }
        }
    }
}

@Composable
fun ResumenScreen(
    registros: List<RegistroAtencion>,
    onNuevaAtencion: () -> Unit,
    onVolverInicio: () -> Unit,
    onNavigateTo: (AppScreen) -> Unit,
    onExit: () -> Unit
) {
    val context = LocalContext.current
    var isLoadingMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val navegacionConCarga: (AppScreen) -> Unit = { screen ->
        scope.launch {
            isLoadingMenu = true
            delay(1000)
            isLoadingMenu = false
            onNavigateTo(screen)
        }
    }

    Scaffold(
        topBar = { VeterinariaTopBar("Historial de Atenciones", navegacionConCarga, onExit) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (registros.isEmpty()) {
                    Text("No hay atenciones registradas.", modifier = Modifier.padding(vertical = 16.dp))
                } else {
                    registros.forEachIndexed { index, registro ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Atención ${index + 1}", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                                Text("Fecha: ${registro.fecha}", fontSize = 12.sp, color = Color.Gray)
                                Text("Veterinario: ${registro.veterinario}", fontSize = 14.sp, fontWeight = FontWeight.Medium)

                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                Text("Dueño: ${registro.dueno.nombre}")
                                Text("Mascota: ${registro.mascota.nombre} (${registro.mascota.especie})")
                                Text("Consulta: ${registro.consulta.descripcion}")

                                if(registro.medicamento.nombre != "N/A") {
                                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                                    Text("Medicamento: ${registro.medicamento.nombre}", fontWeight = FontWeight.SemiBold)
                                    Text("Precio Final: $${registro.precioFinalMedicamento}")

                                    if (registro.detalleDescuento.isNotEmpty()) {
                                        Text(
                                            text = registro.detalleDescuento,
                                            color = Color.Red,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // Botón Compartir (Intent Implícito)
                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        // Intent implícito para compartir
                                        val shareText = buildString {
                                            appendLine("📋 CONSULTA VETERINARIA")
                                            appendLine()
                                            appendLine(" Fecha: ${registro.fecha}")
                                            appendLine(" Veterinario: ${registro.veterinario}")
                                            appendLine()
                                            appendLine(" Mascota: ${registro.mascota.nombre}")
                                            appendLine(" Especie: ${registro.mascota.especie}")
                                            appendLine(" Edad: ${registro.mascota.edad} años")
                                            appendLine("⚖ Peso: ${registro.mascota.peso} kg")
                                            appendLine()
                                            appendLine(" Dueño: ${registro.dueno.nombre}")
                                            appendLine(" Email: ${registro.dueno.email}")
                                            appendLine(" Teléfono: ${registro.dueno.telefono}")
                                            appendLine()
                                            appendLine(" Tipo de consulta: ${registro.consulta.descripcion}")
                                            appendLine(" Costo consulta: $${registro.consulta.costoConsulta}")

                                            if (registro.medicamento.nombre != "N/A") {
                                                appendLine()
                                                appendLine(" Medicamento: ${registro.medicamento.nombre}")
                                                appendLine(" Precio medicamento: $${registro.precioFinalMedicamento}")
                                                if (registro.detalleDescuento.isNotEmpty()) {
                                                    appendLine(" Descuento: ${registro.detalleDescuento}")
                                                }
                                            }

                                            appendLine()
                                            appendLine("---")
                                            appendLine("Veterinaria Duoc UC")
                                        }

                                        val sendIntent = android.content.Intent().apply {
                                            action = android.content.Intent.ACTION_SEND
                                            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                                            putExtra(android.content.Intent.EXTRA_SUBJECT, "Consulta Veterinaria - ${registro.mascota.nombre}")
                                            type = "text/plain"
                                        }

                                        val shareIntent = android.content.Intent.createChooser(sendIntent, "Compartir consulta mediante...")
                                        context.startActivity(shareIntent)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    androidx.compose.material.icons.Icons.Default.Share
                                    Text("📤 Compartir Consulta")
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

            if (isLoadingMenu) {
                PantallaDeCarga(mensaje = "Cargando...")
            }
        }
    }
}

// -----------------------------------------------------------------------
// FORMULARIOS
// -----------------------------------------------------------------------

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
            label = { Text("Nombre dueño") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorNombre.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorEmail = if (!Validaciones.validarEmail(it)) "Email inválido" else "" },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorEmail.isNotEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it
                val d = it.replace(Regex("[^0-9]"), "")
                errorTelefono = if (d.length != 9 || !d.startsWith("9")) "Debe ser 9 dígitos (9xxxxxxxx)" else ""
            },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorTelefono.isNotEmpty()
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
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre mascota") }, modifier = Modifier.fillMaxWidth())
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroConsultaForm(
    tiposAtencion: List<Pair<String, Double>>,
    onComplete: (Consulta) -> Unit,
    onBack: () -> Unit
) {
    var selected by remember { mutableStateOf("") }

    Column {
        tiposAtencion.forEach { (tipo, costo) ->
            Card(
                onClick = { selected = tipo },
                colors = CardDefaults.cardColors(
                    containerColor = if(selected==tipo)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ),
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
                    val costo = tiposAtencion.find { it.first == selected }?.second ?: 0.0
                    onComplete(Consulta((1000..9999).random(), selected, costo, "Agendada"))
                },
                enabled = selected.isNotEmpty()
            ) { Text("Siguiente") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionMedicamentoForm(
    viewModel: RegistroViewModel,
    listaMedicamentos: List<Medicamento>,
    titulo: String,
    onComplete: (Medicamento) -> Unit,
    onBack: () -> Unit
) {
    var selected by remember { mutableStateOf<Medicamento?>(null) }

    Column {
        Text(titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        listaMedicamentos.forEach { med ->
            val precioFinal = viewModel.calcularPrecioFinal(med)
            val detalleDescuento = viewModel.obtenerDetalleDescuento(med)

            Card(
                onClick = { selected = med },
                colors = CardDefaults.cardColors(
                    containerColor = if(selected==med)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected == med, onClick = { selected = med })
                        Text(med.nombre, fontWeight = FontWeight.Bold)
                    }

                    if (precioFinal < med.precio) {
                        // Con descuento
                        Text(
                            text = "Precio normal: $${med.precio.toInt()}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                        Text(
                            text = "Precio oferta: $${precioFinal.toInt()}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                        if (detalleDescuento.isNotEmpty()) {
                            Text(
                                text = detalleDescuento,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 32.dp)
                            )
                        }
                    } else {
                        // Sin descuento
                        Text(
                            text = "Precio: $${precioFinal.toInt()}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 32.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = onBack) { Text("Volver") }
            Button(
                onClick = { selected?.let { onComplete(it) } },
                enabled = selected != null
            ) { Text("Finalizar") }
        }
    }
}

// -----------------------------------------------------------------------
// COMPONENTES VISUALES
// -----------------------------------------------------------------------

@Composable
fun PantallaDeCarga(mensaje: String = "Cargando...") {
    Surface(
        modifier = Modifier.fillMaxSize().zIndex(10f),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.logo1),
                    contentDescription = "Logo Carga",
                    modifier = Modifier.size(200.dp).padding(bottom = 16.dp)
                )
                CircularProgressIndicator(modifier = Modifier.size(40.dp), strokeWidth = 4.dp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = mensaje, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        activity?.let {
            val controller = WindowInsetsControllerCompat(it.window, it.window.decorView)
            controller.hide(WindowInsetsCompat.Type.statusBars())
        }
        delay(2000)
        onSplashFinished()
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.let {
                val controller = WindowInsetsControllerCompat(it.window, it.window.decorView)
                controller.show(WindowInsetsCompat.Type.statusBars())
            }
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer
        )
    )

    Box(
        modifier = Modifier.fillMaxSize().background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val context = LocalContext.current
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(R.drawable.logo2)
                    .crossfade(true)
                    .build(),
                contentDescription = "Logo Veterinaria",
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Veterinaria Duoc UC", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(48.dp), strokeWidth = 4.dp)
        }
    }
}