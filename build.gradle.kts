import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.net.URL

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.rikonardo.papermake") version "1.0.4"
}

group = "net.insprill"
version = getFullVersion()

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.essentialsx.net/releases/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly(fileTree("libs"))
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") { isTransitive = false }
    compileOnly("com.github.MyzelYam:SuperVanish:6.2.7") { isTransitive = false }
    compileOnly("fr.xephi:authme:5.6.0-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("net.essentialsx:EssentialsX:2.19.7")
    compileOnly("net.kyori:adventure-text-minimessage:4.11.0")
    compileOnly("org.jetbrains:annotations:23.0.0")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("net.insprill:XenLib:a6942f042e")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

val extraDependencies = mapOf(
    Pair("CMI-API.jar", "https://github.com/Zrips/CMI-API/releases/download/8.7.8.2/CMIAPI8.7.8.2.jar"),
    Pair("VanishNoPacket.jar", "https://mediafiles.forgecdn.net/files/3661/454/VanishNoPacket.jar"),
)

tasks {

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar {
        enabled = false
    }

    val reloc = task<ConfigureShadowRelocation>("relocateShadowJar") {
        target = shadowJar.get()
        prefix = "net.insprill.cjm.libs"
    }

    shadowJar {
        dependsOn(reloc)
        archiveClassifier.set("")
        minimize()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
    }

    val extraDeps = register("downloadExtraDependencies") {
        val libsDir = File("libs")
        libsDir.mkdirs()
        for (entry in extraDependencies) {
            val file = File(libsDir, entry.key)
            if (file.exists())
                continue
            println("Downloading ${entry.key} from ${entry.value}")
            URL(entry.value).openStream().use { s -> file.outputStream().use { it.write(s.readBytes()) } }
            println("Successfully downloaded ${entry.key} to ${file.path}")
        }
    }

    build {
        dependsOn(extraDeps)
        dependsOn(shadowJar)
    }
}

fun getFullVersion(): String {
    val version = project.property("version")!! as String
    return if (version.contains("-SNAPSHOT")) {
        "$version+rev.${getGitHash()}"
    } else {
        version
    }
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
