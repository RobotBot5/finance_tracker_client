package com.robotbot.finance_tracker_client.get_info

import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.get_info.sources.RemoteGetInfoSource
import javax.inject.Inject

interface GetInfoRepository {

    suspend fun getCurrenciesList(): List<CurrencyEntity>

    suspend fun getIconsList(): List<IconEntity>

    suspend fun getIconById(id: Long): IconEntity

    suspend fun getCurrencyByCode(code: String): CurrencyEntity
}

internal class RealGetInfoRepository @Inject constructor(
    private val getInfoSource: RemoteGetInfoSource
) : GetInfoRepository {

    override suspend fun getCurrenciesList(): List<CurrencyEntity> {
        return getInfoSource.getCurrenciesList()
    }

    override suspend fun getIconsList(): List<IconEntity> {
        return getInfoSource.getIcons()
    }

    override suspend fun getIconById(id: Long): IconEntity {
        return getInfoSource.getIconById(id)
    }

    override suspend fun getCurrencyByCode(code: String): CurrencyEntity {
        return getInfoSource.getCurrencyByCode(code)
    }
}
