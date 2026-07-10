plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.face_aisdk_flutter_plugin_example"
    compileSdk = 34
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        applicationId = "com.example.face_aisdk_flutter_plugin_example"
        minSdk = flutter.minSdkVersion
        targetSdk = 34
        versionCode = flutter.versionCode
        versionName = flutter.versionName
        multiDexEnabled = true
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    implementation(files("../../../android/libs/FaceSDKLib-release.aar"))
    implementation("io.github.faceaisdk:Android:2026.06.25")
    implementation("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.1")
    implementation("com.tencent:mmkv:1.3.14")
    implementation("com.airbnb.android:lottie:6.5.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.core:core:1.10.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("pub.devrel:easypermissions:3.0.0")
    implementation("androidx.multidex:multidex:2.0.1")
}

flutter {
    source = "../.."
}
