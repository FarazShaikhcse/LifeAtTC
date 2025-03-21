package com.faraz.lifeattc.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnalyseContentUseCaseImpl @Inject constructor(): AnalyseContentUseCase {
    override fun countUniqueWords(content: String): Flow<Result<Map<String, Int>>> = flow {
        try {
            val wordCount = mutableMapOf<String, Int>()
            val words = content.split(Regex("\\s+"))
            for (word in words) {
                val cleanWord = word.trim().lowercase()
                if (cleanWord.isNotEmpty()) {
                    wordCount[cleanWord] = wordCount.getOrDefault(cleanWord, 0) + 1
                }
            }

            emit(Result.success(wordCount))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun findFifteenthChar(content: String): Flow<Result<Char>> = flow {
        try {
            val textWithoutWhitespace = content.replace(Regex("\\s+"), "")
            if (textWithoutWhitespace.length >= 15) {
                emit(Result.success(textWithoutWhitespace[14])) // 0-based indexing
            } else {
                emit(Result.failure(IllegalArgumentException("Content is less than 15 characters")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun findEveryFifteenthCharacter(content: String): Flow<Result<List<Char>>> = flow {
        try {
            val result = mutableListOf<Char>()
            val textWithoutWhitespace = content.replace(Regex("\\s+"), "")
            for (i in 14 until textWithoutWhitespace.length step 15) { // 0-based indexing
                result.add(textWithoutWhitespace[i])
            }
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}