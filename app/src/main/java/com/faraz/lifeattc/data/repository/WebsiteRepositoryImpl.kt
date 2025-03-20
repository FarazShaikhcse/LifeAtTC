package com.faraz.lifeattc.data.repository

import com.faraz.lifeattc.data.remote.WebsiteService
import com.faraz.lifeattc.domain.model.WebsiteContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WebsiteRepositoryImpl @Inject constructor(
    private val websiteService: WebsiteService
) : WebsiteRepository {
    override fun getWebsiteContent(url: String): Flow<WebsiteContent> = flow {
        try {
            val response = websiteService.getWebsiteContent(url)
            emit(WebsiteContent(content = response))
        } catch (e: Exception) {
            emit(WebsiteContent(error = e.message ?: "Unknown error"))
        }
    }
}