package com.robotbot.finance_tracker_client

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.robotbot.finance_tracker_client.di.DaggerAppDaggerComponent
import com.robotbot.finance_tracker_client.root.RootContent
import com.robotbot.finance_tracker_client.ui.coil.LocalCoilImageLoader

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))

        val daggerComponent = DaggerAppDaggerComponent.factory()
            .create(application = application)

        val component = daggerComponent
            .rootComponentFactory(defaultComponentContext())

        setContent {
            CompositionLocalProvider(LocalCoilImageLoader provides daggerComponent.imageLoader) {
                MaterialTheme {
                    RootContent(component = component, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}