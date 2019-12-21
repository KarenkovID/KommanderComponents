plugins{
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
    }

//    buildTypes {
//        release {
//            minifyEnabled = true
//            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//        }
//    }

}

dependencies {
    implementation(project(":domain_core"))

    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.appCompat)
    implementation(Libraries.ktxCore)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.material)
    implementation(Libraries.adapterDelegates)
    implementation(Libraries.timber)
//    implementation libraries.timber
//
//    testImplementation testLibraries.junit
//    androidTestImplementation testLibraries.testExtension
//    androidTestImplementation testLibraries.espressoCore
}
