package com.faraz.lifeattc.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.faraz.lifeattc.R
import com.faraz.lifeattc.presentation.WebsiteAnalysisState

@Composable
fun ResultView(uiState: WebsiteAnalysisState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
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
                        text = stringResource(R.string.every_15th_char_heading),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (uiState.everyFifteenthCharacter.isNotEmpty()) {
                        Text(
                            text = stringResource(
                                R.string.every_15th_character,
                                uiState.everyFifteenthCharacter.joinToString()
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
                        text = stringResource(R.string.word_counter_heading),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (uiState.wordCount.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.word_count),
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
        if (uiState.wordCount.isNotEmpty()) {
            items(uiState.wordCount.entries.toList()) { (word, count) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
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