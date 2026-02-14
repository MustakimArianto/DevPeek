package com.mustakimarianto.devpeek.feature_settings.navigation

import kotlinx.serialization.Serializable


interface SettingsRoute {
    @Serializable
    data object Root : SettingsRoute
}
