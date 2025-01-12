/*
 * This file was generated by the Gradle 'init' task.
 */
import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("minecraft-prometheus-exporter.java-conventions")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":core"))
    compileOnly(libs.velocityApi)
    compileOnly(libs.platformDetectionVelocity)

    annotationProcessor(libs.velocityApi)
}

description = "velocity-prometheus-exporter"

tasks {
    shadowJar {
        archiveClassifier = ""
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        expand("version" to project.version)
    }
}
