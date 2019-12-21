val componentsRoot: String by extra

include(":android_core")
include(":domain_core")

project(":android_core").projectDir = File(componentsRoot, "android_core")
project(":domain_core").projectDir = File(componentsRoot, "domain_core")