package com.mustakimarianto.devpeek.feature_saved.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustakimarianto.devpeek.feature_saved.domain.SavedRepository
import com.mustakimarianto.devpeek.feature_saved.domain.SavedState
import com.mustakimarianto.devpeek.feature_search.domain.DetailState
import com.mustakimarianto.devpeek.feature_search.domain.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val savedRepository: SavedRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSavedUsers()
    }

    private fun loadSavedUsers() {
        viewModelScope.launch {
            savedRepository.getSavedUsers()
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            savedState = SavedState.Error(
                                message = e.message ?: "Failed to load saved users"
                            )
                        )
                    }
                }
                .collectLatest { users ->
                    if (users.isEmpty()) {
                        _uiState.update { it.copy(savedState = SavedState.Empty) }
                    } else {
                        // Group users by date saved
                        val groupedUsers = users.groupBy { user ->
                            formatDateHeader(user.createdAt)
                        }
                        _uiState.update {
                            it.copy(savedState = SavedState.Success(groupedUsers))
                        }
                    }
                }
        }
    }

    private fun formatDateHeader(timestamp: String): String {
        return try {
            // Parse the timestamp (assuming ISO format from GitHub API)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val date = dateFormat.parse(timestamp) ?: Date()

            val now = Date()
            val diffMillis = now.time - date.time
            val diffDays = diffMillis / (1000 * 60 * 60 * 24)

            when {
                diffDays < 1 -> "Today"
                diffDays < 2 -> "Yesterday"
                diffDays < 7 -> "This Week"
                diffDays < 30 -> "This Month"
                else -> {
                    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    monthFormat.format(date)
                }
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun deleteSavedUser(userId: Int) {
        viewModelScope.launch {
            try {
                savedRepository.deleteSavedUser(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteAllSavedUsers() {
        viewModelScope.launch {
            try {
                savedRepository.deleteAllSavedUsers()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadUserDetail(username: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(detailState = DetailState.Loading) }

            try {
                // OFFLINE FIRST: Try to load from local database first
                val savedUser = savedRepository.getSavedUserByUsername(username)

                if (savedUser != null) {
                    // User found in local database - show immediately (offline-first)
                    val savedRepos = savedRepository.getSavedRepositories(savedUser.id)

                    _uiState.update {
                        it.copy(
                            detailState = DetailState.Success(
                                user = savedUser,
                                topRepos = savedRepos, // Load from local DB
                                isSaved = true,
                            )
                        )
                    }

                    // Then try to refresh from network in background (optional)
                    try {
                        val freshUserDetail = searchRepository.getUserDetail(username)
                        val topRepos = searchRepository.getUserRepositories(username, limit = 3)

                        _uiState.update {
                            it.copy(
                                detailState = DetailState.Success(
                                    user = freshUserDetail,
                                    topRepos = topRepos,
                                    isSaved = true,
                                )
                            )
                        }

                        // Update local cache with fresh data
                        savedRepository.saveUserWithRepositories(freshUserDetail, topRepos)
                    } catch (e: Exception) {
                        // Network failed, but we already have local data showing
                        // Just silently fail - user still sees offline data
                        e.printStackTrace()
                    }
                } else {
                    // User not in local database - must fetch from network
                    val userDetail = searchRepository.getUserDetail(username)
                    val topRepos = searchRepository.getUserRepositories(username, limit = 3)
                    val isSaved = searchRepository.isUserSaved(userDetail.id)

                    _uiState.update {
                        it.copy(
                            detailState = DetailState.Success(
                                user = userDetail,
                                topRepos = topRepos,
                                isSaved = isSaved,
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        detailState = DetailState.Error(
                            message = e.message ?: "Unknown error occurred"
                        )
                    )
                }
            }
        }
    }

    fun toggleSave() {
        viewModelScope.launch {
            val currentState = _uiState.value.detailState
            if (currentState is DetailState.Success) {
                try {
                    if (currentState.isSaved) {
                        // Unsave: delete user and their repositories
                        savedRepository.deleteSavedUser(currentState.user.id)
                    } else {
                        // Save: store user with their top repositories
                        savedRepository.saveUserWithRepositories(
                            user = currentState.user,
                            repositories = currentState.topRepos
                        )
                    }

                    _uiState.update {
                        it.copy(
                            detailState = currentState.copy(isSaved = !currentState.isSaved)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun clearDetailState() {
        _uiState.update { it.copy(detailState = DetailState.Idle) }
    }

    override fun onCleared() {
        super.onCleared()
        clearDetailState()
    }
}