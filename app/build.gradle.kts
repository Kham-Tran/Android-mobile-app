plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("androidx.room")
}



android {
    namespace = "com.example.stockanalyser204"
    compileSdk = 34


    room {
        schemaDirectory ("D:\\StockAnalyser204\\schemas")
    }

    defaultConfig {
        applicationId = "com.example.stockanalyser204"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

//        kapt{
//            arguments {
//                arg("room.schemaLocation", "com/example/stockanalyser201/LocalDatabase/schemas")
//            }
//        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/INDEX.LIST"
        }
    }

}



val ktorVersion = "2.3.12"
val roomVersion = "2.6.1"
val material3Version = "1.3.0"

dependencies {

    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("androidx.room:room-runtime:${roomVersion}")
    implementation("androidx.room:room-ktx:${roomVersion}")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.6")
//    implementation(libs.androidx.junit.ktx)

    kapt("androidx.room:room-compiler:${roomVersion}")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.fleeksoft.ksoup:ksoup-ktor2:0.1.9")
    implementation("com.opencsv:opencsv:5.7.1")
    // Optional: Include only if you need to use network request functions such as
    // Ksoup.parseGetRequest, Ksoup.parseSubmitRequest, and Ksoup.parsePostRequest
    implementation("com.fleeksoft.ksoup:ksoup-network-ktor2:0.1.9")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}