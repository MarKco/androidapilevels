plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 36

    defaultConfig {
        applicationId = "it.marcozanetti.androidapilevels"
        minSdk = 21
        targetSdk = 36
        versionCode = 13
        versionName = "13"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "it.marcozanetti.androidapilevels"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-scalars:3.0.0")
    implementation("org.jsoup:jsoup:1.21.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")
    implementation("androidx.compose.compiler:compiler:1.5.10")
}
