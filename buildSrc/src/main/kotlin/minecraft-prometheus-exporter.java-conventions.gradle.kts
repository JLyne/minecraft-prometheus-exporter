import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
    `maven-publish`
}

group = "uk.co.notnull"
version = "2.2.6-SNAPSHOT"

//https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.not-null.co.uk/releases/")
    }
}

dependencies {
    implementation(libs.jetty)
    implementation(libs.simpleclientCommon)
    implementation(libs.simpleclientHotspot)
    testImplementation(libs.junit)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks {
    compileJava {
        options.compilerArgs.addAll(listOf("-Xlint:all", "-Xlint:-processing"))
        options.encoding = "UTF-8"
    }
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components.getByName("java"))
            pom {
                url = "https://github.com/JLyne/minecraft-prometheus-exporter"
                developers {
                    developer {
                        id = "jim"
                        name = "James Lyne"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/JLyne/minecraft-prometheus-exporter.git"
                    developerConnection = "scm:git:ssh://github.com/JLyne/minecraft-prometheus-exporter.git"
                    url = "https://github.com/JLyne/minecraft-prometheus-exporter"
                }
            }
        }
    }
    repositories {
        maven {
            name = "notnull"
            credentials(PasswordCredentials::class)
            val releasesRepoUrl = uri("https://repo.not-null.co.uk/releases/") // gradle -Prelease publish
            val snapshotsRepoUrl = uri("https://repo.not-null.co.uk/snapshots/")
            url = if (project.hasProperty("release")) releasesRepoUrl else snapshotsRepoUrl
        }
    }
}