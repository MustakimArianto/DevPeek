package com.mustakimarianto.devpeek.feature_search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mustakimarianto.devpeek.core.data.local.PreferencesManager
import com.mustakimarianto.devpeek.feature_search.domain.SearchRepository
import com.mustakimarianto.devpeek.feature_search.domain.model.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _queryFlow = MutableStateFlow("")
    private val _filterFlow = MutableStateFlow(UserType.ALL)

    // PagingData flow that automatically handles pagination
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pagingDataFlow = combine(_queryFlow, _filterFlow) { query, filter -> query to filter }
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { (query, filter) ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                val apiQuery = when (filter) {
                    UserType.ALL -> query
                    UserType.USER -> "$query type:user"
                    UserType.ORG -> "$query type:org"
                }
                if (query.isNotBlank()) preferencesManager.addRecentSearch(query)
                repository.searchUsersPaging(apiQuery)
            }
        }
        .cachedIn(viewModelScope)

    init {
        observeRecentSearches()
        observeFilter()
    }

    private fun observeRecentSearches() {
        viewModelScope.launch {
            preferencesManager.recentSearches.collectLatest { searches ->
                _uiState.update { it.copy(recentSearches = searches) }
            }
        }
    }

    private fun observeFilter() {
        viewModelScope.launch {
            _filterFlow.collectLatest { filter ->
                _uiState.update { it.copy(filter = filter) }
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        _queryFlow.value = query
    }

    fun onFilterChange(filter: UserType) {
        _filterFlow.value = filter
        // Trigger new search with updated filter
        val currentQuery = _uiState.value.query
        if (currentQuery.isNotBlank()) {
            _queryFlow.value = currentQuery // Re-emit to trigger new search
        }
    }

    fun onRecentSearchClick(query: String) {
        _uiState.update { it.copy(query = query) }
        _queryFlow.value = query
    }

    fun onClearRecentSearches() {
        preferencesManager.clearRecentSearches()
    }
}