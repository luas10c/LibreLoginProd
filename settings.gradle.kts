pluginManagement {
    // The libby gradle plugin is vendored (see build-logic/libby-gradle-plugin);
    // its former home, repo.kyngs.xyz, no longer serves it.
    includeBuild("build-logic/libby-gradle-plugin")
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "LibreLoginProd"
include("Plugin", "API")
