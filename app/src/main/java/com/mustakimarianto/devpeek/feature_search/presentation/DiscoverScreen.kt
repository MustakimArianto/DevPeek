package com.mustakimarianto.devpeek.feature_search.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.mustakimarianto.devpeek.feature_search.domain.model.GithubUser
import com.mustakimarianto.devpeek.feature_search.domain.model.UserType

@Composable
fun DiscoverScreen(
    query: String,
    filter: UserType,
    recentSearches: List<String>,
    lazyPagingItems: LazyPagingItems<GithubUser>,
    onQueryChange: (String) -> Unit,
    onFilterChange: (UserType) -> Unit,
    onRecentSearchClick: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    onUserClick: (GithubUser) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DiscoverHeader()
        SearchBar(
            query = query,
            onQueryChange = { newQuery -> onQueryChange(newQuery) },
            onSearch = { keyboardController?.hide() },
            onClear = { onQueryChange("") },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterChipsRow(
            selected = filter,
            onFilterChange = { newFilter -> onFilterChange(newFilter) },
        )

        Spacer(modifier = Modifier.height(4.dp))

        when {
            // Initial state - no query entered
            query.isBlank() -> {
                IdleContent(
                    recentSearches = recentSearches,
                    onRecentClick = { term -> onRecentSearchClick(term) },
                    onClearRecent = onClearRecentSearches,
                )
            }

            // Initial loading state
            lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                ShimmerList()
            }

            // Error state on initial load
            lazyPagingItems.loadState.refresh is LoadState.Error -> {
                val error = lazyPagingItems.loadState.refresh as LoadState.Error
                ErrorState(
                    message = error.error.message ?: "Unknown error",
                    onRetry = { lazyPagingItems.retry() },
                )
            }

            // Empty state - query returned no results
            lazyPagingItems.itemCount == 0 -> {
                EmptyState(query = query)
            }

            // Success - show paginated list
            else -> {
                PagingResultsList(
                    lazyPagingItems = lazyPagingItems,
                    onUserClick = onUserClick,
                )
            }
        }
    }
}

@Composable
private fun PagingResultsList(
    lazyPagingItems: LazyPagingItems<GithubUser>,
    onUserClick: (GithubUser) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id }
        ) { index ->
            val user = lazyPagingItems[index]
            if (user != null) {
                UserCard(
                    user = user,
                    onClick = { onUserClick(user) }
                )
            }
        }

        // Show loading indicator at the end when loading more
        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }

        // Show error message at the end when append fails
        if (lazyPagingItems.loadState.append is LoadState.Error) {
            item {
                val error = lazyPagingItems.loadState.append as LoadState.Error
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = error.error.message ?: "Failed to load more",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        TextButton(onClick = { lazyPagingItems.retry() }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DiscoverHeader() {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)) {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "GitHub Users",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = "Search GitHub users…",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        )
    )
}

@Composable
private fun FilterChipsRow(
    selected: UserType,
    onFilterChange: (UserType) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(UserType.entries.toTypedArray()) { type ->
            FilterChip(
                label = when (type) {
                    UserType.ALL -> "All"
                    UserType.USER -> "Users"
                    UserType.ORG -> "Organizations"
                },
                isSelected = selected == type,
                onClick = { onFilterChange(type) }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun IdleContent(
    recentSearches: List<String>,
    onRecentClick: (String) -> Unit,
    onClearRecent: () -> Unit,
) {
    if (recentSearches.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "🔍", fontSize = 48.sp)
                Text(
                    text = "Search GitHub Users",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Enter a username to get started",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Recent Searches",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                TextButton(onClick = onClearRecent) {
                    Text("Clear")
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                recentSearches.forEach { search ->
                    Surface(
                        onClick = { onRecentClick(search) },
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = search,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(
    user: GithubUser,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Avatar
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "${user.login}'s avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
            )

            // Name + handle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "@${user.login}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Type badge
            UserTypeBadge(type = user.type)
        }
    }
}

@Composable
private fun UserTypeBadge(type: String) {
    val isOrg = type == "Organization"
    val bgColor = if (isOrg) MaterialTheme.colorScheme.tertiaryContainer
    else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (isOrg) MaterialTheme.colorScheme.onTertiaryContainer
    else MaterialTheme.colorScheme.onSecondaryContainer
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor,
    ) {
        Text(
            text = if (isOrg) "org" else "user",
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
    }
}


@Composable
private fun ShimmerList() {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        userScrollEnabled = false,
    ) {
        items(7) { ShimmerUserCard() }
    }
}

@Composable
private fun ShimmerUserCard() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmerX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerX",
    )
    val brush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            MaterialTheme.colorScheme.surfaceVariant,
        ),
        start = Offset(shimmerX, 0f),
        end = Offset(shimmerX + 300f, 0f),
    )

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(brush)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
            }
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
        }
    }
}

@Composable
private fun EmptyState(query: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "🔍", fontSize = 48.sp)
            Text(
                text = "No results for \"$query\"",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Try a different username or filter",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "⚠️", fontSize = 48.sp)
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}