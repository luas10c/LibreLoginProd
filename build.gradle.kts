plugins {
    id("com.diffplug.spotless") version "7.2.1"
}

defaultTasks("updateLicenses", "shadowJar")

version = "0.26.3"

tasks.register("updateLicenses") {
    dependsOn(subprojects.map { it.tasks.named("spotlessApply") })
}

tasks.register("checkLicenses") {
    dependsOn(subprojects.map { it.tasks.named("spotlessCheck") })
}

subprojects {
    version = rootProject.version
    group = "xyz.kyngs.librelogin"

    apply {
        plugin("com.diffplug.spotless")
    }

    tasks.register("updateLicenses") {
        dependsOn("spotlessApply")
    }

    tasks.register("checkLicenses") {
        dependsOn("spotlessCheck")
    }

    tasks.configureEach {
        if (name.contains("jar", true)) {
            dependsOn("updateLicenses")
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    spotless {
        java {
            googleJavaFormat()
                .aosp()
                .reflowLongStrings()
                .reorderImports(false)
        }
    }
}
