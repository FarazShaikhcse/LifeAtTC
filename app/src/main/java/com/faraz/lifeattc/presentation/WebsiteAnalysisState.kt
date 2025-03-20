package com.faraz.lifeattc.presentation

data class WebsiteAnalysisState(
    val isLoading: Boolean = false,
    val content: String = "",
    val fifteenthCharacter: Char? = null,
    val everyFifteenthCharacter: List<Char> = emptyList(),
    val wordCount: Map<String, Int> = emptyMap(),
    val error: String? = null,
    val showLoadButton: Boolean = true,
)