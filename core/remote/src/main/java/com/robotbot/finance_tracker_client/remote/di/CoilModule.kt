package com.robotbot.finance_tracker_client.remote.di

import android.app.Application
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.svg.SvgDecoder
import com.robotbot.finance_tracker_client.remote.token.AuthInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

@Module
internal interface CoilModule {

    companion object {

        @Provides
        fun provideCoilHttpClient(
            application: Application,
            authInterceptor: AuthInterceptor
        ): ImageLoader {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()

            return ImageLoader.Builder(application)
                .components {
                    add(SvgDecoder.Factory())
                    add(OkHttpNetworkFetcherFactory(okHttpClient))
                }
                .build()
        }
    }
}