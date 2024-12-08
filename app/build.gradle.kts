plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
   // id("com.google.gms.google-services")
    id ("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.tep_timeshareexchangeplatform"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tep_timeshareexchangeplatform"
        minSdk = 30
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    packagingOptions {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/INDEX.LIST"
            )
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.annotation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.material.v190)

    //nav
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")


    // Life cycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")


    // Retrofit
    var retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")


    // OkHttp3
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")


    // Dagger Hilt
    var dagger_hilt_version = "2.48.1"
    implementation ("com.google.dagger:hilt-android:$dagger_hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_hilt_version")


    // Coroutines Version
    var coroutines_version = "1.5.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")


    // =================== Google Services ===========================
    // Gson Version
    implementation("com.google.code.gson:gson:2.10.1")

    // GooglePlay Services Version
   /* implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("androidx.biometric:biometric:1.2.0-alpha05")*/

    // Access Token Outh2
    implementation("com.google.cloud:google-cloud-core:2.27.0")

    // FireBase Version
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-storage")

    // =================== Google Services ===========================



    // ================== Gif and Image View =========================
    // Picasso Version
    implementation("com.squareup.picasso:picasso:2.8")

    // Gif Image View Version
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    // Flexbox version
    implementation("com.google.android.flexbox:flexbox:3.0.0")


    // Paging for android
    implementation("androidx.paging:paging-common-ktx:3.2.1")
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")


    // Google Map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.android.libraries.places:places:3.4.0")


    // swip
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")


    // Lottie
    implementation ("com.airbnb.android:lottie:6.4.0")

    // roundedimageview
    implementation ("com.makeramen:roundedimageview:2.3.0")


    implementation ("com.google.android.flexbox:flexbox:3.0.0")

    // Circle Image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Circle indicator
    implementation ("me.relex:circleindicator:2.1.6")

    //JWT decoder
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // OpenStreetView
    implementation("org.osmdroid:osmdroid-android:6.1.12")

    // Shimmer Loading Effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.wdullaer:materialdatetimepicker:4.2.3")

    // PhotoView
    implementation ("com.github.chrisbanes:PhotoView:2.0.0")

    //MPChart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")








}