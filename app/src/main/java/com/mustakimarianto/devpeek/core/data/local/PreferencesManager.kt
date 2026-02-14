package com.mustakimarianto.devpeek.core.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    context: Context
) {
    companion object {
        private const val PREFS_NAME = "app_preferences"
        private const val KEY_RECENT_SEARCHES = "recent_searches"
        private const val MAX_RECENT_SEARCHES = 5
        private const val DELIMITER = "|||"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _recentSearches = MutableStateFlow(loadRecentSearches())
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    private fun loadRecentSearches(): List<String> {
        val savedString = sharedPreferences.getString(KEY_RECENT_SEARCHES, "") ?: ""
        return if (savedString.isEmpty()) {
            emptyList()
        } else {
            savedString.split(DELIMITER).filter { it.isNotBlank() }
        }
    }

    private fun saveRecentSearches(searches: List<String>) {
        sharedPreferences.edit()
            .putString(KEY_RECENT_SEARCHES, searches.joinToString(DELIMITER))
            .apply()
        _recentSearches.value = searches
    }

    fun addRecentSearch(query: String) {
        if (query.isBlank()) return

        val currentList = _recentSearches.value

        // Remove query if it already exists, then add to front
        val updatedList = (listOf(query) + currentList.filter { it != query })
            .take(MAX_RECENT_SEARCHES)

        saveRecentSearches(updatedList)
    }

    fun clearRecentSearches() {
        sharedPreferences.edit()
            .remove(KEY_RECENT_SEARCHES)
            .apply()
        _recentSearches.value = emptyList()
    }
}