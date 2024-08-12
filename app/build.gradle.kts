plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "uz.ibrohim.amwayuz"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.ibrohim.amwayuz"
        minSdk = 23
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Firebase
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)

    //Navigation Fragment
    implementation (libs.androidx.fragment)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.navigationUi)

    //Language
    implementation(libs.lingver.language)

    //Toast
    implementation (libs.toast.grender)

    //Progress
    implementation(libs.progress.colakcode)

    //Dialog
    implementation(libs.dialog.umeshJangid)

    //Camera
    implementation(libs.imagepicker.dhaval2404)

    //Glide
    annotationProcessor(libs.glide.compiler)
    implementation(libs.glide.bumptech)

    //ViewModel
    implementation(libs.viewModel.lifecycle)
    implementation(libs.viewModel.runtime)
}