package com.faraz.lifeattc.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faraz.lifeattc.R
import com.faraz.lifeattc.presentation.WebsiteAnalysisState
import com.faraz.lifeattc.presentation.WordCountPageData

@Composable
fun ResultView(
    uiState: WebsiteAnalysisState,
    onToggleCardExpansion: (String) -> Unit,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fifteenth Character Section (Always expanded)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.fifteenth_char_heading),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (uiState.fifteenthCharacter != null) {
                        Text(
                            text = stringResource(
                                R.string.fifteenth_character,
                                uiState.fifteenthCharacter
                            ),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        // Every 15th Character Section (Expandable)
        item {
            ExpandableCard(
                title = stringResource(R.string.every_15th_char_heading),
                isExpanded = uiState.expandedCardIds.contains("every15thChar"),
                isLoading = uiState.everyFifteenthCharacter.isEmpty(),
                onToggleExpand = { onToggleCardExpansion("every15thChar") }
            ) {
                if (uiState.everyFifteenthCharacter.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            R.string.every_15th_character,
                            uiState.everyFifteenthCharacter.joinToString()
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Word Count Section (Expandable)
        item {
            ExpandableCard(
                title = stringResource(R.string.word_counter_heading),
                isExpanded = uiState.expandedCardIds.contains("wordCount"),
                isLoading = uiState.wordCountPageData == null,
                onToggleExpand = { onToggleCardExpansion("wordCount") }
            ) {
                uiState.wordCountPageData?.let { pageData ->
                    PaginatedWordCountList(
                        pageData = pageData,
                        onPreviousPage = onPreviousPage,
                        onNextPage = onNextPage
                    )
                }
            }
        }
    }
}

@Composable
fun PaginatedWordCountList(
    pageData: WordCountPageData,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Summary text
        Text(
            text = "Total unique words: ${pageData.totalUniqueWords}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Page navigation
        if (pageData.totalPages > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPreviousPage,
                    enabled = pageData.canGoToPrevious
                ) {
                    Text("Previous")
                }

                Text("Page ${pageData.currentPage + 1} of ${pageData.totalPages}")

                Button(
                    onClick = onNextPage,
                    enabled = pageData.canGoToNext
                ) {
                    Text("Next")
                }
            }
        }

        // Table headers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Word",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Count",
                fontWeight = FontWeight.Bold
            )
        }

        // Word items for current page
        Column {
            pageData.currentPageItems.forEach { (word, count) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = word,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    Text(text = count.toString())
                }
            }
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    isExpanded: Boolean,
    isLoading: Boolean,
    onToggleExpand: () -> Unit,
    content: @Composable () -> Unit
) {
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row with title and chevron
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { if (!isLoading) onToggleExpand() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        modifier = Modifier.rotate(rotationState)
                    )
                }
            }

            // Expandable content
            AnimatedVisibility(visible = isExpanded && !isLoading) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    content()
                }
            }
        }
    }
}