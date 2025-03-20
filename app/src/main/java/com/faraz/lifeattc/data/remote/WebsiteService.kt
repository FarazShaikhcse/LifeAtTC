package com.faraz.lifeattc.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface WebsiteService {
    @GET
    suspend fun getWebsiteContent(@Url url: String): String
}
