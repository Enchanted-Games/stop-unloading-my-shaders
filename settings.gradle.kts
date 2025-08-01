import dev.kikugie.stonecutter.settings.StonecutterSettings

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev")
        maven("https://maven.neoforged.net/releases/")
        maven("https://repo.spongepowered.org/maven")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.kikugie.dev/releases")
    }
    plugins {
        kotlin("jvm") version "2.1.21"
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.6.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
extensions.configure<StonecutterSettings> {
    kotlinController = true
    centralScript = "build.gradle.kts"

    // The versions listed here, commented out or otherwise, all have pre-made gradle.properties.
    shared {
        vers("1.21.8-fabric","1.21.8")
        vers("1.21.8-neoforge","1.21.8")
        vcsVersion="1.21.8-fabric"
    }
    create(rootProject)
}

rootProject.name = "eg_stop_unloading_my_shaders"




