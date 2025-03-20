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
}