@file:Suppress("UnstableApiUsage")
@file:OptIn(StonecutterExperimentalAPI::class)

import dev.kikugie.stonecutter.StonecutterExperimentalAPI


plugins {
    id("net.neoforged.moddev")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

stonecutter {
    val (version, loader) = current.project.split('-', limit = 2)
    properties.tags(version, loader)
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')

val rawModVersion: String = sc.properties["mod.version"]
version = "$rawModVersion+${sc.properties.get<String>("deps.minecraft")}-neoforge"
base.archivesName = sc.properties.get<String>("mod.id")

repositories {
    mavenLocal()
    maven {
        name = "Terraformers (Mod Menu)"
        url = uri("https://maven.terraformersmc.com/releases/")
        content {
            includeGroupAndSubgroups("com.terraformersmc")
        }
    }
    maven {
        name = "Parchment Mappings"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
    }
    maven {
        name = "Gegy (mojbackward)"
        url = uri("https://maven.gegy.dev/releases/")
        content {
            includeGroupAndSubgroups("dev.lambdaurora")
        }
    }
}

dependencies {
}

neoForge {
    version = sc.properties.get<String>("deps.neoforge")
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = sc.properties.get<String>("deps.parchment").split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        register("client") {
            gameDirectory = rootProject.file("run/")
            client()
        }
        register("server") {
            gameDirectory = rootProject.file("run/")
            server()
        }
    }

    mods {
        register(sc.properties.get<String>("mod.id")) {
            sourceSet(sourceSets["main"])
        }
    }
    sourceSets["main"].resources.srcDir("src/main/generated")
}

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String): String = sc.properties[name]

    val neoforgeIconProperty = if(stonecutter.eval(stonecutter.current.version, ">=26.2")) {
        "iconFile"
    } else {
        "logoFile"
    }

    val props = HashMap<String, String>().apply {
        this["version"] = prop("mod.version") + "+" + prop("deps.minecraft")
        this["minecraft"] = prop("dep_str.minecraft")
        this["id"] = prop("mod.id")
        this["group"] = prop("mod.group")
        this["description"] = prop("mod.description")
        this["name"] = prop("mod.name")
        this["website_url"] = prop("mod.website_url")
        this["source_url"] = prop("mod.source_url")
        this["issue_tracker"] = prop("mod.issue_tracker")
        this["icon"] = prop("mod.icon")
        this["license"] = prop("mod.license")
        this["java_ver"] = java.targetCompatibility.majorVersion
        this["neo_icon_property"] = neoforgeIconProperty
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml", "*.mixins.json")) {
        expand(props)
    }
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/*.classtweaker")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${rawModVersion}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">1.21.11")) {
        JavaVersion.VERSION_25
    } else if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
        JavaVersion.VERSION_21
    } else {
        JavaVersion.VERSION_17
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

val additionalVersionsStr = findProperty("publish.additionalVersions") as String?
val additionalVersions: List<String> = additionalVersionsStr
    ?.split(",")
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?: emptyList()

publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    // one of BETA, ALPHA, STABLE
    type = STABLE
    displayName = "[NF] v${rawModVersion} for mc ${sc.properties.get<String>("deps.minecraft")}"
    version = project.version.toString()
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("neoforge")

    dryRun = boolProperty("publish.dry_run")

    if (hasProperty("publish.modrinth")) {
        modrinth {
            projectId = sc.properties.get<String>("publish.modrinth") as String
            accessToken = env.MODRINTH_API_KEY.orNull()
            minecraftVersions.add(sc.properties.get<String>("deps.minecraft"))
            minecraftVersions.addAll(additionalVersions)
            environment = CLIENT_ONLY
        }
    }

    if (hasProperty("publish.curseforge")) {
        curseforge {
            projectId = sc.properties.get<String>("publish.curseforge") as String
            accessToken = env.CURSEFORGE_API_KEY.orNull()
            minecraftVersions.add(stonecutter.current.version)
            minecraftVersions.addAll(additionalVersions)
            client = true
            server = false
        }
    }
}


fun bool(str: String) : Boolean {
    return str.lowercase().startsWith("t")
}

fun boolProperty(key: String) : Boolean {
    if(!hasProperty(key)){
        return false
    }
    return bool(property(key).toString())
}
