import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.2.21"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    implementation("net.insprill:spigot-utils:0.5.1")
}

tasks {
    kotlin {
        // Compile against Java 21, compile to Java 17
        jvmToolchain(21)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}
