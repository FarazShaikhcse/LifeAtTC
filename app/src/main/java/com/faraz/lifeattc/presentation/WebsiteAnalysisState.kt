package com.faraz.lifeattc.presentation

data class WebsiteAnalysisState(
    val fifteenthCharacter: Char? = null,
    val everyFifteenthCharacter: List<Char> = emptyList(),
    val wordCount: Map<String, Int> = emptyMap(),
    val wordCountPageData: WordCountPageData? = null,
    val expandedCardIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showLoadButton: Boolean = true,
)

data class WordCountPageData(
    val totalUniqueWords: Int,
    val totalPages: Int,
    val currentPage: Int,
    val currentPageItems: List<Pair<String, Int>>,
    val canGoToPrevious: Boolean,
    val canGoToNext: Boolean
)