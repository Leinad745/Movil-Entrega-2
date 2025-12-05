plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("jacoco")
}

android {
    namespace = "com.example.animeping"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.animeping"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.8")

    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // ViewModel para Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.20")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("io.mockk:mockk:1.13.8")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.12.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debug dependencies
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //other dependencies
    implementation("androidx.compose.material:material-icons-extended")
}
//JaCoCo
tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest") // Ejecuta los tests antes de generar el reporte

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    // Archivos que queremos excluir del reporte
    val fileFilter = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*",
        "**/Manifest*.*", "**/*Test*.*", "android/**/*",
        "**/model/*", //Excluir modelos de datos simples si quieres
        "**/*databinding/**/*",
        "**/*generated/**/*",
        "**/AnimeUiState.kt",
        "**/MainViewModel.kt",
        "**/remote/*",
        "**/MainActivity.kt"
    )

    val debugTree = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug").get().asFile) {
        exclude(fileFilter)
    }

    // Directorio de tu código fuente
    val mainSrc = "src/main/java"

    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    classDirectories.setFrom(files(debugTree))

    // Donde JaCoCo busca el resultado de los tests
    executionData.setFrom(fileTree(layout.buildDirectory.get().asFile) {
        include(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "jacoco/testDebugUnitTest.exec"
        )
    })
}