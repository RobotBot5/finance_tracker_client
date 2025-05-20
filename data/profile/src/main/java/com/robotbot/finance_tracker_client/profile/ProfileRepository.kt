package com.robotbot.finance_tracker_client.profile

import com.robotbot.finance_tracker_client.profile.entities.ProfileEntity
import com.robotbot.finance_tracker_client.profile.sources.RemoteProfileSource
import javax.inject.Inject

interface ProfileRepository {
    suspend fun getProfile(): ProfileEntity

    suspend fun setTargetCurrency(targetCurrencyCode: String)
}

internal class RealProfileRepository @Inject constructor(
    private val remoteSource: RemoteProfileSource
) : ProfileRepository {

    override suspend fun getProfile(): ProfileEntity {
        return remoteSource.getProfile()
    }

    override suspend fun setTargetCurrency(targetCurrencyCode: String) {
        remoteSource.setTargetCurrency(targetCurrencyCode)
    }
}
