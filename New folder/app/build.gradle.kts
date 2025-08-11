plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.smartcanteen"
    compileSdk = 35
    buildFeatures{
        viewBinding= true
    }


    defaultConfig {
        applicationId = "com.example.smartcanteen"
        minSdk = 33
        targetSdk = 35
        versionCode = 6
        versionName = "6.0"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.fragment)
    implementation(libs.billing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //extra dependencies
    implementation (libs.ccp)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.material.v110)
    implementation (libs.vectordrawable)
    implementation (libs.navigation.fragment)
    implementation (libs.cardview)
    implementation (libs.picasso)
    implementation (libs.recyclerview)
    implementation (libs.firebase.ui.database)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
    implementation (libs.navigation.ui)
    implementation (libs.lifecycle.extensions)
    implementation (libs.legacy.support.v4)
    implementation(libs.firebase.bom)
    implementation(libs.google.firebase.auth)

    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.fragment.ktx)
    implementation (libs.cardview)
    implementation (libs.appcompat)
    implementation (libs.exifinterface)
    implementation (libs.transition)

    implementation ("com.razorpay:checkout:1.6.25")
    implementation ("com.airbnb.android:lottie:5.2.0")

    implementation ("com.sun.mail:android-mail:1.6.2")
    implementation ("com.sun.mail:android-activation:1.6.2")


}