plugins {
    // 👇 SOLUSI: Gunakan alias yang sama dengan root, tapi TANPA 'apply false'
    // Ini akan otomatis memakai versi yang sudah ada di project (2.2.10)
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    // StdLib (Otomatis ikut versi plugin, tapi ditulis eksplisit juga boleh)
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // 👇 PENTING: Coroutines WAJIB 1.8.0 atau lebih baru agar cocok dengan Kotlin 2.x
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Unit Test
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
}