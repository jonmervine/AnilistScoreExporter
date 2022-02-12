import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
//    id("com.apollographql.apollo3").version("3.0.0")
    id("com.apollographql.apollo").version("2.4.0")
}

group = "DarkMage530"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("io.arrow-kt:arrow-core:1.0.1")

//    implementation("com.apollographql.apollo3:apollo-runtime:3.0.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    // The core runtime dependencies
    implementation("com.apollographql.apollo:apollo-runtime:2.4.0")
    // Coroutines extensions for easier asynchronicity handling
    implementation("com.apollographql.apollo:apollo-coroutines-support:2.4.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}

//apollo {
//    packageNamesFromFilePaths()
//}
apollo {
    generateKotlinModels.set(true)
}