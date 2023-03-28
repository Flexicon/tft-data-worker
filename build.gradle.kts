import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.8.10"

    application
    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.serialization") version kotlinVersion
}

group = "com.flexicondev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val kxSVersion = "1.4.0"
    val fuelVersion = "2.3.1"

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kxSVersion")
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-kotlinx-serialization:$fuelVersion")

    testImplementation(kotlin("test"))
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