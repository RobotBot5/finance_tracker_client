package com.robotbot.finance_tracker_client.profile.entities

import com.robotbot.finance_tracker_client.get_info.entities.CurrencyEntity

data class ProfileEntity(
    val email: String,
    val name: String,
    val targetCurrency: CurrencyEntity
)
