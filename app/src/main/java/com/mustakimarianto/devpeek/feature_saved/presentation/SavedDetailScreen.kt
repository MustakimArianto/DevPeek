package com.mustakimarianto.devpeek.feature_saved.presentation

import androidx.compose.runtime.Composable
import com.mustakimarianto.devpeek.core.ui_component.UserDetailScreen
import com.mustakimarianto.devpeek.feature_search.domain.DetailState

@Composable
fun SavedDetailScreen(
    detailState: DetailState,
    onBackClick: () -> Unit,
    onSaveToggle: () -> Unit,
    onRetry: () -> Unit,
) {
    UserDetailScreen(
        detailState = detailState,
        onBackClick = onBackClick,
        onSaveToggle = onSaveToggle,
        onRetry = onRetry
    )
}