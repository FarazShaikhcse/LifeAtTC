package com.faraz.lifeattc.data.repository

import app.cash.turbine.test
import com.faraz.lifeattc.data.remote.WebsiteService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class WebsiteRepositoryImplTest {

    private lateinit var websiteRepository: WebsiteRepositoryImpl
    private lateinit var websiteService: WebsiteService

    @Before
    fun setUp() {
        websiteService = mockk()
        websiteRepository = WebsiteRepositoryImpl(websiteService)
    }

    @Test
    fun `getWebsiteContent emits content on successful service call`() = runTest {
        // Given
        val url = "https://example.com"
        val expectedContent = "<html><body>Example content</body></html>"
        coEvery { websiteService.getWebsiteContent(url) } returns expectedContent

        // When & Then
        websiteRepository.getWebsiteContent(url).test {
            val result = awaitItem()

            assertEquals(expectedContent, result.content)
            assertNull(result.error)

            awaitComplete()
        }
    }

    @Test
    fun `getWebsiteContent emits error message when service call fails with exception message`() = runTest {
        // Given
        val url = "https://example.com"
        val errorMessage = "Connection timeout"
        coEvery { websiteService.getWebsiteContent(url) } throws IOException(errorMessage)

        // When & Then
        websiteRepository.getWebsiteContent(url).test {
            val result = awaitItem()

            assertEquals(errorMessage, result.error)
            assertEquals("", result.content)

            awaitComplete()
        }
    }

    @Test
    fun `getWebsiteContent emits 'Unknown error' when exception has no message`() = runTest {
        // Given
        val url = "https://example.com"
        coEvery { websiteService.getWebsiteContent(url) } throws IOException()

        // When & Then
        websiteRepository.getWebsiteContent(url).test {
            val result = awaitItem()

            assertEquals("Unknown error", result.error)
            assertEquals("", result.content)

            awaitComplete()
        }
    }

    @Test
    fun `getWebsiteContent correctly handles RuntimeException`() = runTest {
        // Given
        val url = "https://example.com"
        val errorMessage = "Parsing error"
        coEvery { websiteService.getWebsiteContent(url) } throws RuntimeException(errorMessage)

        // When & Then
        websiteRepository.getWebsiteContent(url).test {
            val result = awaitItem()

            assertEquals(errorMessage, result.error)
            assertEquals("", result.content)

            awaitComplete()
        }
    }

    @Test
    fun `getWebsiteContent correctly handles IllegalArgumentException`() = runTest {
        // Given
        val url = "invalid-url"
        val errorMessage = "Invalid URL format"
        coEvery { websiteService.getWebsiteContent(url) } throws IllegalArgumentException(errorMessage)

        // When & Then
        websiteRepository.getWebsiteContent(url).test {
            val result = awaitItem()

            assertEquals(errorMessage, result.error)
            assertEquals("", result.content)

            awaitComplete()
        }
    }

    @Test
    fun `getWebsiteContent works with empty response`() = runTest {
        // Given
        val url = "https://example.com/empty"
        val emptyContent = ""
        coEvery { websiteService.getWebsiteContent(url) } returns emptyContent

        // When & Then
        websiteRepository.getWebsiteContent(url).test {
            val result = awaitItem()

            assertEquals(emptyContent, result.content)
            assertNull(result.error)

            awaitComplete()
        }
    }
}