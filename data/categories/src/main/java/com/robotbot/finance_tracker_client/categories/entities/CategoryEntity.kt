package com.robotbot.finance_tracker_client.categories.entities

import com.robotbot.finance_tracker_client.get_info.entities.IconEntity

data class CategoryEntity(
    val id: Long,
    val name: String,
    val type: CategoryType,
    val isSystem: Boolean,
    val icon: IconEntity
)
