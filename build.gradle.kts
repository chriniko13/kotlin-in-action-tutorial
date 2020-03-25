import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.70"
}

group = "com.chriniko"
version = "1.0-SNAPSHOT"

dependencies {

    compile(kotlin("stdlib-jdk8"))

    compile ("com.google.inject:guice:4.1.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

}

repositories {
    mavenCentral()
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}