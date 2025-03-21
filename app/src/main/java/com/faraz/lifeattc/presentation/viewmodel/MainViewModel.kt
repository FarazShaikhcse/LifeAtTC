package com.faraz.lifeattc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faraz.lifeattc.data.repository.WebsiteRepository
import com.faraz.lifeattc.domain.usecase.AnalyseContentUseCaseImpl
import com.faraz.lifeattc.presentation.WebsiteAnalysisState
import com.faraz.lifeattc.presentation.WordCountPageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TC_WEB_URL =
    "https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val websiteRepository: WebsiteRepository,
    private val analyseContentUseCase: AnalyseContentUseCaseImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(WebsiteAnalysisState())
    val uiState: StateFlow<WebsiteAnalysisState> = _uiState.asStateFlow()

//    var uiState by mutableStateOf(WebsiteAnalysisState())
//        private set

    private var currentPage = 0
    private val noOfWordsPerPage = 20

//    fun analyzeWebsite(url: String) {
//        viewModelScope.launch {
//            state = state.copy(isLoading = true, error = null)
//
//            try {
//                val result = analyzeWebsiteUseCase(url)
//
//                state = state.copy(
//                    fifteenthCharacter = result.fifteenthCharacter,
//                    everyFifteenthCharacter = result.everyFifteenthCharacter,
//                    wordCount = result.wordCount,
//                    isLoading = false
//                )
//
//                // Initialize word count page data
//                refreshWordCountPageData()
//
//            } catch (e: Exception) {
//                state = state.copy(
//                    isLoading = false,
//                    error = e.message ?: "An unknown error occurred"
//                )
//            }
//        }
//    }

    fun toggleCardExpansion(cardId: String) {
        val currentExpandedCards = _uiState.value.expandedCardIds.toMutableSet()
        if (currentExpandedCards.contains(cardId)) {
            currentExpandedCards.remove(cardId)
        } else {
            currentExpandedCards.add(cardId)
        }

        _uiState.value = _uiState.value.copy(expandedCardIds = currentExpandedCards)
    }

    fun goToNextPage() {
        if (_uiState.value.wordCountPageData?.canGoToNext == true) {
            currentPage++
            refreshWordCountPageData()
        }
    }

    fun goToPreviousPage() {
        if (_uiState.value.wordCountPageData?.canGoToPrevious == true) {
            currentPage--
            refreshWordCountPageData()
        }
    }

    private fun refreshWordCountPageData() {
        val wordCountData = calculateWordCountPageData(currentPage, noOfWordsPerPage)
        _uiState.value = _uiState.value.copy(wordCountPageData = wordCountData)
    }

    private fun calculateWordCountPageData(page: Int, noOfWordsPerPage: Int): WordCountPageData {
        val sortedEntries = _uiState.value.wordCount.entries.sortedByDescending { it.value }
        val totalItems = sortedEntries.size
        val totalPages = (totalItems + noOfWordsPerPage - 1) / noOfWordsPerPage

        val startIndex = page * noOfWordsPerPage
        val endIndex = minOf(startIndex + noOfWordsPerPage, totalItems)
        val currentPageItems = sortedEntries
            .subList(startIndex, endIndex)
            .map { it.key to it.value }

        return WordCountPageData(
            totalUniqueWords = totalItems,
            totalPages = totalPages,
            currentPage = page,
            currentPageItems = currentPageItems,
            canGoToPrevious = page > 0,
            canGoToNext = page < totalPages - 1
        )
    }

    fun loadWebsiteContent(url: String = TC_WEB_URL) {
        _uiState.value = WebsiteAnalysisState(isLoading = true, showLoadButton = false)

        viewModelScope.launch {
            websiteRepository.getWebsiteContent(url).collect { websiteContent ->
                if (websiteContent.error == null) {
                    _uiState.value = _uiState.value.copy(
//                        content = websiteContent.content,
                        isLoading = false
                    )
                    performAnalysis(websiteContent.content)
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = websiteContent.error,
                        isLoading = false,
                        showLoadButton = true,
                    )
                }
            }
        }
    }

    private fun performAnalysis(content: String) {
        viewModelScope.launch {
            analyseContentUseCase.findFifteenthChar(content).collect { result ->
                result.fold(
                    onSuccess = { chars ->
                        _uiState.value = _uiState.value.copy(fifteenthCharacter = chars)
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(error = error.message)
                    }
                )
            }
        }

        viewModelScope.launch {
            analyseContentUseCase.findEveryFifteenthCharacter(content).collect { result ->
                result.fold(
                    onSuccess = { chars ->
                        _uiState.value = _uiState.value.copy(everyFifteenthCharacter = chars)
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(error = error.message)
                    }
                )
            }
        }

        viewModelScope.launch {
            analyseContentUseCase.countUniqueWords(content).collect { result ->
                result.fold(
                    onSuccess = { wordCount ->
                        _uiState.value = _uiState.value.copy(wordCount = wordCount)
                        refreshWordCountPageData()
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(error = error.message)
                    }
                )
            }
        }
    }
}