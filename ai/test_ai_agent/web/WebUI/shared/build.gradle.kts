@file:Suppress("DEPRECATION")

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    // -------------------------------------------- //
    // !!! Required for Preview rendering !!!
    // -------------------------------------------- //
    alias(libs.plugins.androidMultiplatformLibrary)

    // BuildConfig object for Kotlin Multiplatform
    id("com.codingfeline.buildkonfig")

    // Protobuf for wasm
    id("com.squareup.wire")

    alias(libs.plugins.kotlin.serialization)
}

wire {
    kotlin {
        // Generates pure Kotlin models compatible with commonMain
    }
}

kotlin {

    // -------------------------------------------- //
    // !!! Required for Preview rendering !!!
    // -------------------------------------------- //
    androidLibrary {
        namespace = "com.yourdomain.app"
        compileSdk = 36
        minSdk = 24

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    // Ref: https://github.com/yshrsmz/BuildKonfig
    buildkonfig {

        packageName = "com.web.ui"

        defaultConfigs {

            val host = project.properties["websocket.host"].toString()
            val port = project.properties["websocket.port"].toString()

            buildConfigField(STRING, "websocketHost", host)
            buildConfigField(STRING, "websocketPort", port)
        }

    }
    
    sourceSets {
        commonMain.dependencies {

            // Compose UI
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.material.icons.extended)

            // Ktor Core & WebSockets plugin
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.websockets)

            // JSON serialization for WebSocket payloads
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)

            // Protobuf for wasm
            implementation(libs.wire.runtime)

            // Nav3
            implementation(libs.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
        }
        // -------------------------------------------- //
        // !!! Required for Preview rendering !!!
        androidMain.dependencies {
            // Needed by ComposeViewAdapter to instantiate previews
            implementation(libs.androidx.ui.tooling)
        }
        // -------------------------------------------- //
        wasmJsMain.dependencies {
//            implementation(libs.kotlinx.browser)
        }
    }
}