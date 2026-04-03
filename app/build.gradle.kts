plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.kacper.projektgramatematyczna"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.kacper.projektgramatematyczna"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    configurations.all {
        resolutionStrategy {
            // Zawsze używaj Guava jako źródła ListenableFuture
            force("com.google.guava:guava:18.0")
            // Wyklucz listenablefuture z wszystkich transitive dependencies
            exclude("com.google.guava", "listenablefuture")
        }
    }
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.gregcockroft:AndroidMath:v1.1.0")
    implementation("com.google.guava:guava:31.1-android")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}