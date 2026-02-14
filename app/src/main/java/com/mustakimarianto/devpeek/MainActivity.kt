package com.mustakimarianto.devpeek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.mustakimarianto.devpeek.navigation.AppNavHost
import com.mustakimarianto.devpeek.ui.theme.DevPeekTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevPeekTheme {
                val navController = rememberNavController()

                AppNavHost(navController)
            }
        }
    }
}