package com.robotbot.finance_tracker_client.get_info.sources.base

import com.robotbot.finance_tracker_client.get_info.sources.dto.CurrencyDto
import com.robotbot.finance_tracker_client.get_info.sources.dto.CurrencyResponse
import com.robotbot.finance_tracker_client.get_info.sources.dto.IconDto
import com.robotbot.finance_tracker_client.get_info.sources.dto.IconResponse
import retrofit2.http.GET
import retrofit2.http.Path

internal interface InfoApi {

    @GET("currencies")
    suspend fun getCurrenciesList(): CurrencyResponse

    @GET("icons")
    suspend fun getIconsList(): IconResponse

    @GET("icons/get/{iconId}")
    suspend fun getIconById(@Path("iconId") iconId: Long): IconDto

    @GET("currencies/{code}")
    suspend fun getCurrencyByCode(@Path("code") code: String): CurrencyDto
}