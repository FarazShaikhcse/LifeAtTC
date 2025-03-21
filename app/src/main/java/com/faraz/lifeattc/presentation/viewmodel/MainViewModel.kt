package com.faraz.lifeattc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faraz.lifeattc.data.repository.WebsiteRepository
import com.faraz.lifeattc.domain.usecase.AnalyseContentUseCaseImpl
import com.faraz.lifeattc.presentation.WebsiteAnalysisState
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

    fun loadWebsiteContent(url: String = TC_WEB_URL) {
        _uiState.value = WebsiteAnalysisState(isLoading = true, showLoadButton = false)

        viewModelScope.launch {
            websiteRepository.getWebsiteContent(url).collect { websiteContent ->
                if (websiteContent.error == null) {
                    _uiState.value = _uiState.value.copy(
                        content = websiteContent.content,
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
                    },
                    onFailure = { error ->
                        _uiState.value = _uiState.value.copy(error = error.message)
                    }
                )
            }
        }
    }
}