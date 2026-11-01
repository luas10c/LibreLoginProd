plugins {
    id("com.diffplug.spotless") version "8.10.4"
}

defaultTasks("updateLicenses", "shadowJar")

version = "0.26.4"

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
            // Must be >= 1.30.0 to run on JDK 25+ (javac internals changed).
            googleJavaFormat("1.36.1")
                .aosp()
                .reflowLongStrings()
                .reorderImports(false)
        }
    }
}
