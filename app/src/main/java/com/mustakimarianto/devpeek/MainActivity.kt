package com.mustakimarianto.devpeek

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.mustakimarianto.devpeek.core.data.local.PreferencesManager
import com.mustakimarianto.devpeek.core.ui_component.RequestNotificationPermission
import com.mustakimarianto.devpeek.navigation.AppNavHost
import com.mustakimarianto.devpeek.ui.theme.DevPeekTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by preferencesManager.isDarkMode.collectAsStateWithLifecycle()

            DevPeekTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()

                RequestNotificationPermission()
                AppNavHost(navController)
            }
        }
    }
}