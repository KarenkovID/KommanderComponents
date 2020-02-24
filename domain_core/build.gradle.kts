plugins {
    id("kotlin")
}

dependencies {
    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.rxJava)
    implementation(Libraries.rxKotlin)

    implementation(kotlin(TestLibraries.kotlinTest))
    implementation(kotlin(TestLibraries.kotlinJunit5))

    api(Libraries.timber)
}
