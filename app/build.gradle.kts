plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}
val compose_version = "1.1.1"

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "ru.boringowl.myroadmapapp"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
//            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildTypes.forEach {
        it.buildConfigField("String", "MAIN_LINK", "\"https://myroadmapserver.herokuapp.com/\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = compose_version
    }
    packagingOptions {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
    namespace = "ru.boringowl.myroadmapapp"
}

dependencies {
    //android
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material3:material3:1.0.0-alpha12")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.paging:paging-compose:1.0.0-alpha14")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.material:material-icons-core:$compose_version")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")
    implementation("androidx.navigation:navigation-compose:2.4.2")
    // Kotlin Extensions
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Accompanist
    implementation("com.google.accompanist:accompanist-placeholder-material:0.24.9-beta")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.9-beta")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.40.1")
    implementation("com.google.android.material:material:1.6.0")
    kapt("com.google.dagger:hilt-android-compiler:2.40.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Third-Party
    implementation("io.github.microutils:kotlin-logging:1.12.5")
    implementation("io.coil-kt:coil-compose:2.0.0-rc03")

    //Room lib
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-paging:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    implementation("androidx.room:room-ktx:2.4.2")
    //data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //retrofit

    //retrofit - networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")


    //tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$compose_version")
    debugImplementation("androidx.compose.ui:ui-tooling:$compose_version")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$compose_version")
}