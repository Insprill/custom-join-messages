import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.Properties

plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.rikonardo.papermake") version "1.0.4"
}

group = "net.insprill"
version = getFullVersion()

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot-API
    maven("https://jitpack.io") // SimplixStorage, SuperVanish, Vault
    maven("https://repo.aikar.co/content/groups/aikar/") // ACF
    maven("https://repo.codemc.org/repository/maven-public/") // AuthMe
    maven("https://repo.essentialsx.net/releases/") // EssentialsX
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
    maven("https://repo.minebench.de/") // MineDown
    maven("https://repo.papermc.io/repository/maven-public/") // MockBukkit
}

dependencies {
    // Plugins
    compileOnly(fileTree("libs")) // No Maven repos :/
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") { isTransitive = false }
    compileOnly("com.github.MyzelYam:SuperVanish:6.2.12") { isTransitive = false }
    compileOnly("fr.xephi:authme:5.6.0-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("net.essentialsx:EssentialsX:2.19.7")

    // Internal
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("com.github.simplix-softworks:simplixstorage:3.2.5")
    implementation("de.themoep:minedown:1.7.1-SNAPSHOT")
    implementation("net.insprill:spigot-utils:0.2.1")
    implementation("net.swiftzer.semver:semver:1.2.0")
    implementation("org.bstats:bstats-bukkit:3.0.0")

    // Tests
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:2.132.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
}

val extraDependencies = mapOf(
    Pair("CMI-API.jar", "https://github.com/Zrips/CMI-API/releases/download/8.7.8.2/CMIAPI8.7.8.2.jar"),
    Pair("VanishNoPacket.jar", "https://mediafiles.forgecdn.net/files/3661/454/VanishNoPacket.jar"),
    Pair("VelocityVanish.jar", "https://github.com/Syrent/VelocityVanish/releases/download/v3.6.1/VelocityVanish.v3.6.1.jar"),
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
        from("LICENSE")
        minimize()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") {
            expand("version" to version)
        }
        doLast {
            File("$buildDir/resources/main/cjm.metadata").bufferedWriter().use {
                val p = Properties()
                p["version"] = project.version
                p["bstats.id"] = project.property("bstats.id")
                p["spigot.resource.id"] = project.property("spigot.resource.id")
                p.store(it, null)
            }
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

    test {
        useJUnitPlatform()
    }
}

configurations {
    configurations.testImplementation.get().apply {
        extendsFrom(configurations.compileOnly.get())
        exclude("org.spigotmc", "spigot-api")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

fun getFullVersion(): String {
    val version = project.property("version")!! as String
    return if (version.contains("-SNAPSHOT")) {
        "$version+rev.${getGitRevision()}"
    } else {
        version
    }
}

fun getGitRevision(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}
