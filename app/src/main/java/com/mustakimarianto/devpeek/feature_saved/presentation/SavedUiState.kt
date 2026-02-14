package com.mustakimarianto.devpeek.feature_saved.presentation

import com.mustakimarianto.devpeek.feature_saved.domain.SavedState
import com.mustakimarianto.devpeek.feature_search.domain.DetailState

data class SavedUiState(
    val savedState: SavedState = SavedState.Loading,
    val detailState: DetailState = DetailState.Idle,
)