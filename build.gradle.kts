import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI
import java.util.concurrent.Executors

plugins {
    kotlin("jvm") version "2.2.21"
    id("org.ajoberstar.grgit") version "5.3.3"
    id("net.kyori.blossom") version "2.2.0"
    id("com.gradleup.shadow") version "9.2.2"
    id("com.modrinth.minotaur") version "2.8.10"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
    id("com.rikonardo.papermake") version "1.0.6"
}

group = "net.insprill"
version = "${project.version}${versionMetadata()}"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot-API
    maven("https://jitpack.io") // AdvancedVanish, SimplixStorage, SuperVanish, VanishNoPacket, Vault
    maven("https://repo.aikar.co/content/groups/aikar/") // ACF
    maven("https://repo.codemc.org/repository/maven-public/") // AuthMe
    maven("https://repo.essentialsx.net/releases/") // EssentialsX
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") // PlaceholderAPI
    maven("https://repo.minebench.de/") // MineDown
    maven("https://repo.papermc.io/repository/maven-public/") // MockBukkit
    maven("https://repo.sayandev.org/snapshots") // SayanVanish
}

dependencies {
    // Plugins
    compileOnly(fileTree("libs")) // No Maven repos :/
    compileOnly("com.github.mbax:VanishNoPacket:3.22") { isTransitive = false }
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") { isTransitive = false }
    compileOnly("com.github.MyzelYam:SuperVanish:6.2.19") { isTransitive = false }
    compileOnly("com.github.Syrent:VelocityVanish:3.27.2") { isTransitive = false }
    compileOnly("com.github.quantiom:AdvancedVanish:1.2.6") { isTransitive = false }
    compileOnly("fr.xephi:authme:5.6.0") { isTransitive = false }
    compileOnly("me.clip:placeholderapi:2.11.7") { isTransitive = false }
    compileOnly("net.essentialsx:EssentialsX:2.21.2") { isTransitive = false }
    compileOnly("org.sayandev:sayanvanish-api:1.6.3") { isTransitive = false }
    compileOnly("org.sayandev:sayanvanish-bukkit:1.6.3") { isTransitive = false }

    // Internal
    compileOnly("org.spigotmc:spigot-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.25.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.25.0")
    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("com.github.simplix-softworks:simplixstorage:3.2.7")
    implementation("de.themoep:minedown:1.7.1-SNAPSHOT")
    implementation("net.insprill:spigot-utils:0.5.1")
    implementation("net.swiftzer.semver:semver:2.1.0")
    implementation("org.bstats:bstats-bukkit:3.1.0")

    // Modules
    implementation(project(":paper"))

    // Tests
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.69.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.3")
    testImplementation(platform("org.junit:junit-bom:5.13.3"))
}

val extraDependencies = mapOf(
    Pair("CMI-API.jar", "https://github.com/Zrips/CMI-API/releases/download/9.7.14.3/CMI-API9.7.14.3.jar"),
)

tasks {
    kotlin {
        // Compile against Java 21, compile to Java 8
        jvmToolchain(21)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        enableAutoRelocation = true
        relocationPrefix = "net.insprill.cjm.libs"
        exclude("META-INF/**")
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
                URI.create(entry.value).toURL().openStream().use { s -> file.outputStream().use { it.write(s.readBytes()) } }
                println("Successfully downloaded ${entry.key} to ${file.path}")
            }
        }
        ex.shutdown()
        ex.awaitTermination(2, TimeUnit.MINUTES)
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

sourceSets {
    main {
        blossom {
            javaSources {
                property("bstatsId", project.property("bstats.id") as String)
                property("hangarProjectId", project.property("hangar.project.id") as String)
                property("modrinthProjectId", project.property("modrinth.project.id") as String)
                property("spigotResourceId", project.property("spigot.resource.id") as String)
                property("buildVersion", version as String)
                property("buildTargetPlatform", project.property("build.target-platform") as String)
            }
        }
    }
}

val minecraftVersions = arrayOf(
    "1.9",
    "1.9.1",
    "1.9.2",
    "1.9.3",
    "1.9.4",
    "1.10",
    "1.10.1",
    "1.10.2",
    "1.11",
    "1.11.1",
    "1.11.2",
    "1.12",
    "1.12.1",
    "1.12.2",
    "1.13",
    "1.13.1",
    "1.13.2",
    "1.14",
    "1.14.1",
    "1.14.2",
    "1.14.3",
    "1.14.4",
    "1.15",
    "1.15.1",
    "1.15.2",
    "1.16",
    "1.16.1",
    "1.16.2",
    "1.16.3",
    "1.16.4",
    "1.16.5",
    "1.17",
    "1.17.1",
    "1.18",
    "1.18.1",
    "1.18.2",
    "1.19",
    "1.19.1",
    "1.19.2",
    "1.19.3",
    "1.19.4",
    "1.20",
    "1.20.1",
    "1.20.2",
    "1.20.3",
    "1.20.4",
    "1.20.5",
    "1.20.6",
    "1.21",
    "1.21.1",
    "1.21.2",
    "1.21.3",
    "1.21.4",
    "1.21.5",
    "1.21.6",
    "1.21.7",
    "1.21.8",
    "1.21.9",
    "1.21.10"
)

modrinth {
    changelog.set(readChangelog(project.version as String))
    token.set(System.getenv("MODRINTH_API_TOKEN") ?: findProperty("modrinthToken") as String?)
    projectId.set(property("modrinth.project.id") as String)
    versionType.set(if ((findProperty("build.is-release") as String? ?: "true").toBoolean()) "release" else "alpha")
    uploadFile.set(tasks.shadowJar.get())
    loaders.addAll("spigot", "paper", "folia", "purpur")
    syncBodyFrom.set(file("modrinth_page.md").readText())
    gameVersions.addAll(*minecraftVersions)
}

hangarPublish {
    publications.register("plugin") {
        version = project.version as String
        id = "Custom-Join-Messages"
        channel = if ((findProperty("build.is-release") as String? ?: "true").toBoolean()) "Release" else "Snapshot"
        changelog = readChangelog(project.version as String)
        apiKey = System.getenv("HANGAR_API_TOKEN") ?: findProperty("hangarToken") as String?
        pages {
            resourcePage(file("modrinth_page.md").readText())
        }
        platforms {
            paper {
                jar = tasks.shadowJar.flatMap { it.archiveFile }
                platformVersions.addAll(*minecraftVersions)
            }
        }
    }
}

fun versionMetadata(): String {
    if (!property("version.metadata").toString().toBoolean()) {
        return ""
    }

    val head = grgit.head()
    var id = head.abbreviatedId

    if (!grgit.status().isClean) {
        id += ".dirty"
    }

    return "+rev.${id}"
}

fun readChangelog(version: String): String {
    val lines = file("CHANGELOG.md").readLines()
    val out = StringBuilder()
    var inVersion = false
    for (line in lines) {
        if (line.startsWith("## [$version] - ")) {
            inVersion = true
            continue
        }
        if (inVersion) {
            if (line.startsWith("## ")) {
                break
            }
            out.append(line).append("\n")
        }
    }

    if (out.isBlank()) {
        out.append("[${grgit.head().abbreviatedId}](${grgit.remote.list().first().url}/commit/${grgit.head().id}): ${grgit.head().fullMessage}")
    }

    return out.toString().trim()
}
