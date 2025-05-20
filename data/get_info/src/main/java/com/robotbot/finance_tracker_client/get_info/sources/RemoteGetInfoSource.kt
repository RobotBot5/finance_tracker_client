package com.robotbot.finance_tracker_client.get_info.sources

import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity
import com.robotbot.finance_tracker_client.get_info.entities.IconEntity
import com.robotbot.finance_tracker_client.get_info.sources.base.InfoApi
import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapper
import javax.inject.Inject

interface RemoteGetInfoSource {

    suspend fun getCurrenciesList(): List<CurrencyEntity>

    suspend fun getIcons(): List<IconEntity>

    suspend fun getIconById(id: Long): IconEntity

    suspend fun getCurrencyByCode(code: String): CurrencyEntity
}

internal class RealRemoteGetInfoSource @Inject constructor(
    private val api: InfoApi,
    private val wrapper: RemoteExceptionsWrapper
) : RemoteGetInfoSource {

    override suspend fun getCurrenciesList(): List<CurrencyEntity> =
        wrapper.wrapRetrofitExceptions {
        api.getCurrenciesList().toEntities()
    }

    override suspend fun getIcons(): List<IconEntity> = wrapper.wrapRetrofitExceptions {
        api.getIconsList().toEntities()
    }

    override suspend fun getIconById(id: Long): IconEntity = wrapper.wrapRetrofitExceptions {
        api.getIconById(id).toEntity()
    }

    override suspend fun getCurrencyByCode(code: String): CurrencyEntity =
        wrapper.wrapRetrofitExceptions {
        api.getCurrencyByCode(code).toEntity()
    }
}
