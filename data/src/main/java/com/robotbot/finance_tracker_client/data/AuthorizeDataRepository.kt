package com.robotbot.finance_tracker_client.data

interface AuthorizeDataRepository {

    suspend fun login(email: String, password: String)

    fun logout()

    suspend fun register(email: String, password: String, firstName: String)
}