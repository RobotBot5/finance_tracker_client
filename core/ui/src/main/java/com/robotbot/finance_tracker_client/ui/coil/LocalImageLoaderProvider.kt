package com.robotbot.finance_tracker_client.ui.coil

import androidx.compose.runtime.compositionLocalOf
import coil3.ImageLoader

val LocalCoilImageLoader = compositionLocalOf<ImageLoader> {
    error("No ImageLoader provided")
}