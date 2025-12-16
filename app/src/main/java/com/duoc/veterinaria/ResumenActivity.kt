package com.duoc.veterinaria

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.veterinaria.ui.ResumenScreen
import com.duoc.veterinaria.ui.navigation.AppScreen
import com.duoc.veterinaria.ui.theme.VeterinariaAppTheme
import com.duoc.veterinaria.viewmodel.ConsultaViewModel

class ResumenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VeterinariaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ConsultaViewModel = viewModel()
                    val registros by viewModel.registros.observeAsState(emptyList())

                    ResumenScreen(
                        registros = registros,
                        onNuevaAtencion = {
                            // Navegar a RegistroActivity
                            val intent = Intent(this, RegistroActivity::class.java)
                            startActivity(intent)
                        },
                        onVolverInicio = {
                            // Volver a MainActivity
                            finish()
                        },
                        onNavigateTo = { screen ->
                            when (screen) {
                                AppScreen.Welcome -> {
                                    finish()
                                }
                                AppScreen.Registro -> {
                                    val intent = Intent(this, RegistroActivity::class.java)
                                    startActivity(intent)
                                }
                                else -> {}
                            }
                        },
                        onExit = {
                            finishAffinity() // Cierra toda la app
                        }
                    )
                }
            }
        }
    }
}