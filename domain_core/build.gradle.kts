plugins {
    id("kotlin")
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.rxJava)
    implementation(Libraries.rxKotlin)

    testImplementation(TestLibraries.junit4)

    api(Libraries.timber)
}
