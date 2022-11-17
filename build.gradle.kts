import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.concurrent.Executors

plugins {
    kotlin("jvm") version "1.7.21"
    id("net.kyori.blossom") version "1.3.1"
    id("com.modrinth.minotaur") version "2.4.5"
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
    testImplementation("com.github.seeseemelk:MockBukkit-v1.19:2.133.1")
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
    }

    val extraDeps = register("downloadExtraDependencies") {
        val libsDir = File("libs")
        libsDir.mkdirs()
        val ex = Executors.newCachedThreadPool()
        for (entry in extraDependencies) {
            val file = File(libsDir, entry.key)
            if (file.exists())
                continue
            ex.submit {
                println("Downloading ${entry.key} from ${entry.value}")
                URL(entry.value).openStream().use { s -> file.outputStream().use { it.write(s.readBytes()) } }
                println("Successfully downloaded ${entry.key} to ${file.path}")
            }
        }
        ex.shutdown()
        ex.awaitTermination(10, TimeUnit.SECONDS)
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

blossom {
    val metadata = "src/main/kotlin/net/insprill/cjm/Metadata.kt"
    fun repl(token: String, value: Any?) {
        replaceToken("\"{$token}\"", "\"$value\"", metadata)
    }
    repl("version", version)
    repl("bstats.id", project.property("bstats.id"))
    repl("spigot.resource.id", project.property("spigot.resource.id"))
    repl("modrinth.project.id", project.property("modrinth.project.id"))
    repl("build.target-platform", project.property("build.target-platform"))
}

modrinth {
    changelog.set(readChangelog(project.version as String))
    token.set(findProperty("modrinthToken") as String?)
    projectId.set(property("modrinth.project.id") as String)
    versionType.set(getModrinthVersionType())
    uploadFile.set(tasks.shadowJar.get())
    loaders.addAll("spigot", "paper", "purpur")
    syncBodyFrom.set(file("modrinth_page.md").readText())
    gameVersions.addAll(
        "1.9.0",
        "1.9.1",
        "1.9.2",
        "1.9.3",
        "1.9.4",
        "1.10.0",
        "1.10.1",
        "1.10.2",
        "1.11.0",
        "1.11.1",
        "1.11.2",
        "1.12.0",
        "1.12.1",
        "1.12.2",
        "1.13.0",
        "1.13.1",
        "1.13.2",
        "1.14.0",
        "1.14.1",
        "1.14.2",
        "1.14.3",
        "1.14.4",
        "1.15.0",
        "1.15.1",
        "1.15.2",
        "1.16.0",
        "1.16.1",
        "1.16.2",
        "1.16.3",
        "1.16.4",
        "1.16.5",
        "1.17.0",
        "1.17.1",
        "1.18.0",
        "1.18.1",
        "1.18.2",
        "1.19.0",
        "1.19.1",
        "1.19.2",
        "1.19.3",
    )
}

fun getFullVersion(): String {
    val version = property("version")!! as String
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

fun getModrinthVersionType(): String {
    val ver = (version as String).toLowerCase()
    return if (ver.contains("snapshot")) {
        "alpha"
    } else if (ver.contains("rc.")) {
        "beta"
    } else {
        "release"
    }
}

fun readChangelog(version: String): String {
    val lines = file("CHANGELOG.txt").readLines()
    val out = StringBuilder()
    var inVersion = false
    for (line in lines) {
        if (line.endsWith("$version:")) {
            inVersion = true
            continue
        }
        if (line.isBlank())
            break
        if (inVersion) {
            out.append(line).append("\n")
        }
    }
    return out.toString()
}
