plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    id("com.codingfeline.buildkonfig") version "0.22.0" apply false
    id("com.squareup.wire") version "7.0.0-alpha05" apply false
    alias(libs.plugins.kotlin.serialization) apply false
}