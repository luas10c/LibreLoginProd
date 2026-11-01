/*
 * Vendored copy of kyngs/libby-gradle-plugin 1.2.1 (commit 879f00c, MPL-2.0 --
 * the same licence as this project), wired in as an included build from
 * settings.gradle.kts.
 *
 * The published artifact behind the "xyz.kyngs.libby.plugin" id is no longer
 * served by repo.kyngs.xyz (the upstream repository was archived on
 * 2026-08-19; only the plugin marker is left, the jar and pom return 404), so
 * a fresh clone of this project cannot resolve its build script classpath.
 * Keeping the four source files here makes the build self-contained. Nothing
 * in the plugin itself was changed.
 */
plugins {
    `java-gradle-plugin`
}

group = "xyz.kyngs.libby"
version = "1.2.1"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.grack:nanojson:1.7")
    compileOnly("com.gradleup.shadow:shadow-gradle-plugin:9.6.1")
}

gradlePlugin {
    plugins {
        create("libby") {
            id = "xyz.kyngs.libby.plugin"
            implementationClass = "xyz.kyngs.libby.plugin.LibbyGradlePlugin"
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
