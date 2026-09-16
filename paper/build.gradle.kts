import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.20"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.3.build.+")
    implementation("net.insprill:spigot-utils:0.6.1")
}

tasks {
    kotlin {
        // Compile against Java 25, compile to Java 8
        jvmToolchain(25)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
}
