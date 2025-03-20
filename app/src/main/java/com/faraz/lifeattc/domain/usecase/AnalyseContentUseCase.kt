package com.faraz.lifeattc.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnalyseContentUseCase @Inject constructor() {
    fun countUniqueWords(content: String): Flow<Result<Map<String, Int>>> = flow {
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

    fun findFifteenthWord(content: String): Flow<Result<Char>> = flow {
        try {
            if (content.length >= 15) {
                emit(Result.success(content[14])) // 0-based indexing
            } else {
                emit(Result.failure(IllegalArgumentException("Content is less than 15 characters")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}