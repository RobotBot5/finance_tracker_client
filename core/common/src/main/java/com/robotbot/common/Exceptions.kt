package com.robotbot.common

open class AppException(
    message: String = "",
    cause: Throwable? = null,
) : RuntimeException(message, cause)

class ConnectionException(cause: Throwable) : AppException(cause = cause)

open class BackendException(
    val code: Int,
    message: String
) : AppException(message = message)

class AuthException(
    message: String
) : BackendException(code = 401, message = message)

class ParseBackendResponseException(
    cause: Throwable
) : AppException(cause = cause)

open class StorageException(
    cause: Throwable
) : AppException(cause = cause)