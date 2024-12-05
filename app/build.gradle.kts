plugins {
    alias(libs.plugins.android.application)
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.appdelivery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.appdelivery"
        minSdk = 24
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

   // implementation ("androidx.core:core:1.15.0") // Atualize para a última versão


    implementation (platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:17.0.0")
    implementation ("com.google.firebase:firebase-appcheck:17.0.0")
    implementation ("com.google.firebase:firebase-appcheck-debug:16.1.2")

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    // TODO: Add specific Firebase dependencies based on your needs
    // Example: Firebase Authentication
    // implementation("com.google.firebase:firebase-auth")
}