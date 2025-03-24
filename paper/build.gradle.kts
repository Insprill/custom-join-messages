plugins {
    kotlin("jvm") version "2.1.10"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("net.insprill:spigot-utils:0.5.0")
}
