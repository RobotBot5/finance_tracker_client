package com.robotbot.finance_tracker_client.remote.token

import android.app.Application
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

interface PrefsAuthDataSource {

    fun saveToken(jwtToken: JwtToken)

    fun readToken(): JwtToken

    fun clearToken()
}

internal class RealPrefsAuthDataSource @Inject constructor(
    application: Application
): PrefsAuthDataSource {

    companion object {
        private const val PREFS_FILENAME = "encrypted_prefs"
        private const val KEY_JWT_TOKEN = "jwt_token"
    }

    private val sharedPreferences by lazy {
        EncryptedSharedPreferences(
            application,
            PREFS_FILENAME,
            MasterKey(application)
        )
    }

    override fun saveToken(jwtToken: JwtToken) {
        sharedPreferences.edit().putString(KEY_JWT_TOKEN, jwtToken.token).apply()
    }

    override fun readToken(): JwtToken {
        return JwtToken(
            sharedPreferences.getString(KEY_JWT_TOKEN, null)
        )
    }

    override fun clearToken() {
        sharedPreferences.edit().remove(KEY_JWT_TOKEN).apply()
    }
}