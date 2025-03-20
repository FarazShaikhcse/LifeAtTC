package com.faraz.lifeattc.data.repository

import com.faraz.lifeattc.domain.model.WebsiteContent
import kotlinx.coroutines.flow.Flow

interface WebsiteRepository {
    fun getWebsiteContent(url: String): Flow<WebsiteContent>
}