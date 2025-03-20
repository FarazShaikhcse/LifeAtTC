package com.faraz.lifeattc.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faraz.lifeattc.data.repository.WebsiteRepository
import com.faraz.lifeattc.presentation.WebsiteAnalysisState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val websiteRepository: WebsiteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WebsiteAnalysisState())
    val uiState: StateFlow<WebsiteAnalysisState> = _uiState.asStateFlow()

    fun loadWebsiteContent(url: String = "https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer") {
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            websiteRepository.getWebsiteContent(url).collect { websiteContent ->
                if (websiteContent.error == null) {
                    _uiState.value = _uiState.value.copy(
                        content = websiteContent.content,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = websiteContent.error,
                        isLoading = false
                    )
                }
            }
        }
    }
}