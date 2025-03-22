package com.faraz.lifeattc.domain.usecase

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AnalyseContentUseCaseImplTest {

    private lateinit var analyseContentUseCase: AnalyseContentUseCaseImpl

    @Before
    fun setUp() {
        analyseContentUseCase = AnalyseContentUseCaseImpl()
    }

    // Tests for countUniqueWords method
    @Test
    fun `countUniqueWords should correctly count unique words`() = runTest {
        // Given
        val content = "hello world hello test test test world"

        // When & Then
        analyseContentUseCase.countUniqueWords(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val wordCount = result.getOrNull()!!
            assertEquals(3, wordCount.size)
            assertEquals(2, wordCount["hello"])
            assertEquals(2, wordCount["world"])
            assertEquals(3, wordCount["test"])

            awaitComplete()
        }
    }

    @Test
    fun `countUniqueWords should handle empty content`() = runTest {
        // Given
        val content = ""

        // When & Then
        analyseContentUseCase.countUniqueWords(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val wordCount = result.getOrNull()!!
            assertEquals(0, wordCount.size)

            awaitComplete()
        }
    }

    @Test
    fun `countUniqueWords should handle spaces only content`() = runTest {
        // Given
        val content = "   "

        // When & Then
        analyseContentUseCase.countUniqueWords(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val wordCount = result.getOrNull()!!
            assertEquals(0, wordCount.size)

            awaitComplete()
        }
    }

    @Test
    fun `countUniqueWords should ignore case`() = runTest {
        // Given
        val content = "Hello hello HELLO World world"

        // When & Then
        analyseContentUseCase.countUniqueWords(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val wordCount = result.getOrNull()!!
            assertEquals(2, wordCount.size)
            assertEquals(3, wordCount["hello"])
            assertEquals(2, wordCount["world"])

            awaitComplete()
        }
    }

    @Test
    fun `countUniqueWords should handle special characters and trim words`() = runTest {
        // Given
        val content = "hello, world! hello-test test."

        // When & Then
        analyseContentUseCase.countUniqueWords(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val wordCount = result.getOrNull()!!
            assertEquals(4, wordCount.size)
            assertEquals(1, wordCount["hello,"])
            assertEquals(1, wordCount["world!"])
            assertEquals(1, wordCount["hello-test"])
            assertEquals(1, wordCount["test."])

            awaitComplete()
        }
    }

    // Tests for findFifteenthChar method
    @Test
    fun `findFifteenthChar should return the 15th character`() = runTest {
        // Given
        val content = "This is a string with more than 15 characters"

        // When & Then
        analyseContentUseCase.findFifteenthChar(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals('i', result.getOrNull())

            awaitComplete()
        }
    }

    @Test
    fun `findFifteenthChar should return error for content less than 15 characters`() = runTest {
        // Given
        val content = "Too short"

        // When & Then
        analyseContentUseCase.findFifteenthChar(content).test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is IllegalArgumentException)
            assertEquals("Content is less than 15 characters", result.exceptionOrNull()?.message)

            awaitComplete()
        }
    }

    @Test
    fun `findFifteenthChar should handle exactly 15 characters`() = runTest {
        // Given
        val content = "Exactly15Chars!"

        // When & Then
        analyseContentUseCase.findFifteenthChar(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)
            assertEquals('!', result.getOrNull())

            awaitComplete()
        }
    }

    // Tests for findEveryFifteenthCharacter method
    @Test
    fun `findEveryFifteenthCharacter should return every 15th character`() = runTest {
        // Given
        val content = "This is a long test string that has multiple fifteen character intervals for testing"

        // When & Then
        analyseContentUseCase.findEveryFifteenthCharacter(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val chars = result.getOrNull()
            assertEquals(listOf('t', 'u', 'h', 'l'), chars)

            awaitComplete()
        }
    }

    @Test
    fun `findEveryFifteenthCharacter should handle content less than 15 characters`() = runTest {
        // Given
        val content = "Too short"

        // When & Then
        analyseContentUseCase.findEveryFifteenthCharacter(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val chars = result.getOrNull()!!
            assertEquals(emptyList<Char>(), chars)

            awaitComplete()
        }
    }

    @Test
    fun `findEveryFifteenthCharacter should handle exactly 15 characters`() = runTest {
        // Given
        val content = "Exactly15Chars!"

        // When & Then
        analyseContentUseCase.findEveryFifteenthCharacter(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val chars = result.getOrNull()!!
            assertEquals(listOf('!'), chars)

            awaitComplete()
        }
    }

    @Test
    fun `findEveryFifteenthCharacter should handle exactly 30 characters`() = runTest {
        // Given
        val content = "This string has thirty chars!!"

        // When & Then
        analyseContentUseCase.findEveryFifteenthCharacter(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val chars = result.getOrNull()!!
            assertEquals(listOf('h'), chars)

            awaitComplete()
        }
    }

    @Test
    fun `findEveryFifteenthCharacter should handle empty content`() = runTest {
        // Given
        val content = ""

        // When & Then
        analyseContentUseCase.findEveryFifteenthCharacter(content).test {
            val result = awaitItem()
            assertTrue(result.isSuccess)

            val chars = result.getOrNull()!!
            assertEquals(emptyList<Char>(), chars)

            awaitComplete()
        }
    }
}