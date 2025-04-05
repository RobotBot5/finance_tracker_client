package com.robotbot.finance_tracker_client.data.authorize.sources.remote.base

import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.robotbot.common.BackendException
import com.robotbot.common.ConnectionException
import com.robotbot.common.ParseBackendResponseException
import retrofit2.HttpException
import java.io.IOException

internal suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: JsonIOException) {
        throw ParseBackendResponseException(e)
    } catch (e: JsonSyntaxException) {
        throw ParseBackendResponseException(e)
    } catch (e: HttpException) {
        throw BackendException(e.code(), "Unknown error")
    } catch (e: IOException) {
        throw ConnectionException(e)
    }
}