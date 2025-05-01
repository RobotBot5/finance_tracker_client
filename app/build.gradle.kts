plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.robotbot.finance_tracker_client"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.robotbot.finance_tracker_client"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.decompose.core)
    implementation(libs.decompose.compose)

    implementation(libs.dagger2)
    ksp(libs.dagger2.compiler)

    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.logging)
    implementation(libs.mvikotlin.coroutines)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.loggingInterceptor)

    implementation(project(":data:authorize"))
    implementation(project(":data:bank_accounts"))
    implementation(project(":data:get_info"))
    implementation(project(":data:categories"))
    implementation(project(":data:transactions"))

    implementation(project(":features:authorize"))
    implementation(project(":features:root"))
    implementation(project(":features:bank_accounts"))
    implementation(project(":features:manage_accounts"))
    implementation(project(":features:currency_choose"))
    implementation(project(":features:icon_choose"))
    implementation(project(":features:categories"))
    implementation(project(":features:manage_categories"))
    implementation(project(":features:create_transfer"))
    implementation(project(":features:transactions"))

    implementation(project(":core:dependencies"))
    implementation(project(":core:remote"))
    implementation(project(":core:ui"))

    implementation(libs.coil.compose)
}