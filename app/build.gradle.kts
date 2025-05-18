plugins {
    alias(libs.plugins.android.application)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.flafla"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.flafla"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    /* Firebase */
    // BoM
    implementation(platform(libs.firebase.bom))
    // Analytics
    implementation(libs.firebase.analytics)
    // Database
    implementation(libs.firebase.firestore)
    // Authentication
    implementation(libs.firebase.auth)

    /* Google */
    implementation(libs.play.services.maps)

    //Glide
    implementation(libs.glide)
    implementation(libs.dotsindicator)

    /* Style */
    implementation(libs.flexbox)
    implementation(libs.transition)
    implementation(libs.recyclerview)

    /* Article */
    // Markdown
    implementation(libs.core)
    implementation(libs.coil)
    implementation(libs.markwon.image.coil)
}