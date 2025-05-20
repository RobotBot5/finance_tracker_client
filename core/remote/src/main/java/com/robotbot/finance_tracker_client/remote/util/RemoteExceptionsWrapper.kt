package com.robotbot.finance_tracker_client.remote.util

import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.robotbot.common.AuthException
import com.robotbot.common.BackendException
import com.robotbot.common.ConnectionException
import com.robotbot.common.ParseBackendResponseException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface RemoteExceptionsWrapper {

    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T

    val authFailFlow: SharedFlow<Unit>
}

internal class RemoteExceptionsWrapperImpl @Inject constructor() : RemoteExceptionsWrapper {

    private val _authFailFlow: MutableSharedFlow<Unit> = MutableSharedFlow()
    override val authFailFlow: SharedFlow<Unit> = _authFailFlow.asSharedFlow()

    override suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: JsonIOException) {
            throw ParseBackendResponseException(e)
        } catch (e: JsonSyntaxException) {
            throw ParseBackendResponseException(e)
        } catch (e: HttpException) {
            throw when {
                e.code() == 401 -> {
                    _authFailFlow.emit(Unit)
                    AuthException("Auth failed")
                }

                else -> BackendException(e.code(), "Unknown error: ${e.code()}")
            }
        } catch (e: IOException) {
            throw ConnectionException(e)
        }
    }
}
