package com.faraz.lifeattc.domain.usecase

import kotlinx.coroutines.flow.Flow

interface AnalyseContentUseCase {
    fun countUniqueWords(content: String): Flow<Result<Map<String, Int>>>
    fun findFifteenthChar(content: String): Flow<Result<Char>>
    fun findEveryFifteenthCharacter(content: String): Flow<Result<List<Char>>>
}