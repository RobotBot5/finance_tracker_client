package com.robotbot.finance_tracker_client.profile.sources

import com.robotbot.finance_tracker_client.profile.entities.ProfileEntity
import com.robotbot.finance_tracker_client.profile.sources.remote.base.ProfileApi
import com.robotbot.finance_tracker_client.profile.sources.remote.dto.UpdateTargetCurrencyRequest
import com.robotbot.finance_tracker_client.remote.util.RemoteExceptionsWrapper
import javax.inject.Inject

internal interface RemoteProfileSource {

    suspend fun getProfile(): ProfileEntity

    suspend fun setTargetCurrency(targetCurrencyCode: String)
}

internal class RealRemoteProfileSource @Inject constructor(
    private val api: ProfileApi,
    private val wrapper: RemoteExceptionsWrapper
) : RemoteProfileSource {

    override suspend fun getProfile(): ProfileEntity = wrapper.wrapRetrofitExceptions {
        api.getProfile().toEntity()
    }

    override suspend fun setTargetCurrency(targetCurrencyCode: String) =
        wrapper.wrapRetrofitExceptions {
            api.updateTargetCurrency(UpdateTargetCurrencyRequest(targetCurrencyCode))
        }
}
