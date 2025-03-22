package com.faraz.lifeattc.presentation.viewmodel

import app.cash.turbine.test
import com.faraz.lifeattc.data.repository.WebsiteRepository
import com.faraz.lifeattc.domain.model.WebsiteContent
import com.faraz.lifeattc.domain.usecase.AnalyseContentUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    @RelaxedMockK
    private lateinit var websiteRepository: WebsiteRepository
    @RelaxedMockK
    private lateinit var analyseContentUseCase: AnalyseContentUseCaseImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(websiteRepository, analyseContentUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadWebsiteContent sets isLoading to true initially`() = runTest {
        // Given
        val url = TC_WEB_URL
        setupContentRepositorySuccess(url, "Sample content")

        // When
        viewModel.loadWebsiteContent(url)

        // Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(true, initialState.isLoading)
            assertEquals(false, initialState.showLoadButton)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadWebsiteContent updates state with content on success`() = runTest {
        // Given
        val url = TC_WEB_URL
        val content = "Sample content"
        setupContentRepositorySuccess(url, content)
        setupAnalysisUseCaseSuccess(content)

        // When
        viewModel.loadWebsiteContent(url)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
//            assertEquals(content, finalState.content)
            assertEquals(false, finalState.isLoading)
            assertEquals(null, finalState.error)
            cancelAndIgnoreRemainingEvents()
        }

        // Verify analysis methods were called
        verify {
            analyseContentUseCase.findFifteenthChar(content)
            analyseContentUseCase.findEveryFifteenthCharacter(content)
            analyseContentUseCase.countUniqueWords(content)
        }
    }

    @Test
    fun `loadWebsiteContent updates state with error on failure`() = runTest {
        // Given
        val url = TC_WEB_URL
        val errorMessage = "Failed to load content"

        coEvery { websiteRepository.getWebsiteContent(url) } returns flow {
            emit(WebsiteContent(error = errorMessage))
        }

        // When
        viewModel.loadWebsiteContent(url)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(errorMessage, finalState.error)
            assertEquals(false, finalState.isLoading)
            assertEquals(true, finalState.showLoadButton)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `performAnalysis updates fifteenthCharacter on success`() = runTest {
        // Given
        val content = "Sample content"
        val fifteenthWord = 't'

        setupContentRepositorySuccess(TC_WEB_URL, content)

        coEvery {
            analyseContentUseCase.findFifteenthChar(content)
        } returns flow {
            emit(Result.success(fifteenthWord))
        }

        coEvery {
            analyseContentUseCase.findEveryFifteenthCharacter(content)
        } returns flow {
            emit(Result.success(emptyList()))
        }

        coEvery {
            analyseContentUseCase.countUniqueWords(content)
        } returns flow {
            emit(Result.success(emptyMap()))
        }

        // When
        viewModel.loadWebsiteContent()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(fifteenthWord, finalState.fifteenthCharacter)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `performAnalysis updates everyFifteenthCharacter on success`() = runTest {
        // Given
        val content = "Sample content"
        val everyFifteenth = listOf('t','e','i')

        setupContentRepositorySuccess(TC_WEB_URL, content)

        coEvery {
            analyseContentUseCase.findFifteenthChar(content)
        } returns flow {
            emit(Result.success('t'))
        }

        coEvery {
            analyseContentUseCase.findEveryFifteenthCharacter(content)
        } returns flow {
            emit(Result.success(everyFifteenth))
        }

        coEvery {
            analyseContentUseCase.countUniqueWords(content)
        } returns flow {
            emit(Result.success(mapOf()))
        }

        // When
        viewModel.loadWebsiteContent()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(everyFifteenth, finalState.everyFifteenthCharacter)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `performAnalysis updates wordCount on success`() = runTest {
        // Given
        val content = "Sample content"
        val wordCount = mapOf("sample" to 1, "content" to 1)

        setupContentRepositorySuccess(TC_WEB_URL, content)

        coEvery {
            analyseContentUseCase.findFifteenthChar(content)
        } returns flow {
            emit(Result.success('t'))
        }

        coEvery {
            analyseContentUseCase.findEveryFifteenthCharacter(content)
        } returns flow {
            emit(Result.success(listOf('t','e','i')))
        }

        coEvery {
            analyseContentUseCase.countUniqueWords(content)
        } returns flow {
            emit(Result.success(wordCount))
        }

        // When
        viewModel.loadWebsiteContent()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(wordCount, finalState.wordCount)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `performAnalysis handles errors for fifteenthWord`() = runTest {
        // Given
        val content = "Sample content"
        val errorMessage = "Failed to find fifteenth word"

        setupContentRepositorySuccess(TC_WEB_URL, content)

        coEvery {
            analyseContentUseCase.findFifteenthChar(content)
        } returns flow {
            emit(Result.failure<Char>(Exception(errorMessage)))
        }

        coEvery {
            analyseContentUseCase.findEveryFifteenthCharacter(content)
        } returns flow {
            emit(Result.success(listOf('t','e','i')))
        }

        coEvery {
            analyseContentUseCase.countUniqueWords(content)
        } returns flow {
            emit(Result.success(mapOf()))
        }

        // When
        viewModel.loadWebsiteContent()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(errorMessage, finalState.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // Helper methods
    private fun setupContentRepositorySuccess(url: String, content: String) {
        coEvery { websiteRepository.getWebsiteContent(url) } returns flow {
            emit(WebsiteContent(content = content))
        }
    }

    private fun setupAnalysisUseCaseSuccess(content: String) {
        coEvery {
            analyseContentUseCase.findFifteenthChar(content)
        } returns flow {
            emit(Result.success('t'))
        }

        coEvery {
            analyseContentUseCase.findEveryFifteenthCharacter(content)
        } returns flow {
            emit(Result.success(listOf('t','e','i')))
        }

        coEvery {
            analyseContentUseCase.countUniqueWords(content)
        } returns flow {
            emit(Result.success(mapOf()))
        }
    }
}