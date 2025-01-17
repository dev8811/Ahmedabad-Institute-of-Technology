plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.ahmedabadinstituteoftechnology"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ahmedabadinstituteoftechnology"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
  


    viewBinding {
        enable = true
    }
    dataBinding {
        enable = true
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
        viewBinding = true
    }
}



dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    androidTestImplementation (libs.androidx.junit.v111)
    implementation (libs.firebase.firestore.v2481)
    implementation (libs.poi)// For reading Excel files
    implementation (libs.poi.ooxml)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.junit.junit)
    androidTestImplementation (libs.androidx.core)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation (libs.androidx.junit.v115)
    implementation (libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    implementation(libs.circleimageview)
    implementation (libs.sdp.android)
    // progress bar




}

