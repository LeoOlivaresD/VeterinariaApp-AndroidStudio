package com.duoc.veterinaria

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.duoc.veterinaria.ui.RegistroScreen
import com.duoc.veterinaria.ui.navigation.AppScreen
import com.duoc.veterinaria.ui.theme.VeterinariaAppTheme
import com.duoc.veterinaria.viewmodel.RegistroViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.jvm.java

class RegistroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VeterinariaAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: RegistroViewModel = viewModel()

                    RegistroScreen(
                        viewModel = viewModel,
                        onRegistroComplete = {
                            // Navegar a ResumenActivity después de guardar
                            val intent = Intent(this, ResumenActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onBackClick = {
                            // Volver a MainActivity
                            finish()
                        },
                        onNavigateTo = { screen ->
                            when (screen) {
                                AppScreen.Welcome -> {
                                    finish()
                                }
                                AppScreen.Resumen -> {
                                    val intent = Intent(this, ResumenActivity::class.java)
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