package com.faraz.lifeattc.domain.usecase

import kotlinx.coroutines.flow.Flow

interface AnalyseContentUseCase {
    fun countUniqueWords(content: String): Flow<Result<Map<String, Int>>>
    fun findFifteenthWord(content: String): Flow<Result<Char>>
    fun findEveryFifteenthCharacter(content: String): Flow<Result<List<Char>>>
}