@file:Suppress("UnstableApiUsage")
@file:OptIn(StonecutterExperimentalAPI::class)

import dev.kikugie.stonecutter.StonecutterExperimentalAPI


plugins {
    id("net.fabricmc.fabric-loom")
    id("me.modmuss50.mod-publish-plugin")
    id("maven-publish")
}

stonecutter {
    val (version, loader) = current.project.split('-', limit = 2)
    properties.tags(version, loader)
}

val minecraft = stonecutter.current.version
val mcVersion = stonecutter.current.project.substringBeforeLast('-')
val classTweakerFilepath = "src/main/resources/${sc.properties.get<String>("mod.id")}.classtweaker"

val rawModVersion: String = sc.properties["mod.version"]
version = "$rawModVersion+${sc.properties.get<String>("deps.minecraft")}-fabric"
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
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.properties.get<String>("deps.minecraft")}")
    implementation("net.fabricmc:fabric-loader:${sc.properties.get<String>("deps.fabric-loader")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${sc.properties.get<String>("deps.fabric-api")}")

    // Mod Menu
    if (hasProperty("deps.modmenu")) {
        api("com.terraformersmc:modmenu:${sc.properties.get<String>("deps.modmenu")}")
    } else {
        compileOnly("com.terraformersmc:modmenu:18.0.0-alpha.8")
    }
}

loom {
    if(project.file(classTweakerFilepath).exists()) {
        accessWidenerPath = project.file(classTweakerFilepath)
    }

    runConfigs.all {
        runDir = "../../run"
        isIdeConfigGenerated = true
    }
}

configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:${sc.properties.get<String>("deps.fabric-loader")}")
    }
}

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String): String = sc.properties[name]

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
        this["fabric_loader_dep_str"] = prop("dep_str.fabric-loader")
        this["fabric_api_dep_str"] = prop("dep_str.fabric-api")
        this["java_ver"] = java.targetCompatibility.majorVersion
    }

    if(project.file(classTweakerFilepath).exists()) {
        props["accesswidener_field"] = "\"accessWidener\": \"${prop("mod.id")}.classtweaker\","
    } else {
        props["accesswidener_field"] = ""
    }

    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml", "META-INF/mods.toml", "*.mixins.json")) {
        expand(props)
    }
}

tasks.named("processResources") {
    dependsOn(":${stonecutter.current.project}:stonecutterGenerate")
}

tasks {
    processResources {
        exclude("**/neoforge.mods.toml", "**/mods.toml", "**/accesstransformer.cfg", "neoforge.mods.toml", "mods.toml", "accesstransformer.cfg")
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
    val javaCompat = JavaVersion.VERSION_25
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
    displayName = "[Fabric] v${rawModVersion} for mc ${sc.properties.get<String>("deps.minecraft")}"
    version = project.version.toString()
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("fabric")

    dryRun = boolProperty("publish.dry_run")

    if(hasProperty("publish.modrinth")) {
        modrinth {
            projectId = sc.properties.get<String>("publish.modrinth")
            accessToken = env.MODRINTH_API_KEY.orNull()
            minecraftVersions.add(sc.properties.get<String>("deps.minecraft"))
            minecraftVersions.addAll(additionalVersions)
            requires("fabric-api")
            optional("modmenu")
            environment = CLIENT_ONLY
        }
    }

    if(hasProperty("publish.curseforge")) {
        curseforge {
            projectId = sc.properties.get<String>("publish.curseforge") as String
            accessToken = env.CURSEFORGE_API_KEY.orNull()
            minecraftVersions.add(stonecutter.current.version)
            minecraftVersions.addAll(additionalVersions)
            requires("fabric-api")
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
    return bool(sc.properties.get<String>(key))
}
