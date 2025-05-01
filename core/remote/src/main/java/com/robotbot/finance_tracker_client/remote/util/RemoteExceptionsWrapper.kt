package com.robotbot.finance_tracker_client.remote.util

import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.robotbot.common.AuthException
import com.robotbot.common.BackendException
import com.robotbot.common.ConnectionException
import com.robotbot.common.ParseBackendResponseException
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: JsonIOException) {
        throw ParseBackendResponseException(e)
    } catch (e: JsonSyntaxException) {
        throw ParseBackendResponseException(e)
    } catch (e: HttpException) {
        throw when {
            e.code() == 401 -> AuthException("Auth failed")
            else -> BackendException(e.code(), "Unknown error: ${e.code()}")
        }
    } catch (e: IOException) {
        throw ConnectionException(e)
    }
}