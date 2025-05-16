plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.robotbot.finance_tracker_client.root"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        buildConfig = true
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

    implementation(project(":features:authorize"))
    implementation(project(":features:bank_accounts"))
    implementation(project(":features:manage_accounts"))
    implementation(project(":features:currency_choose"))
    implementation(project(":features:icon_choose"))
    implementation(project(":features:categories"))
    implementation(project(":features:manage_categories"))
    implementation(project(":features:create_transfer"))
    implementation(project(":features:transactions"))
    implementation(project(":features:analytics"))
    implementation(project(":core:ui"))
}