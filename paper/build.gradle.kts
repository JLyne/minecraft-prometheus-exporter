import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("minecraft-prometheus-exporter.java-conventions")
    alias(libs.plugins.pluginYmlPaper)
}

dependencies {
    implementation(project(":core"))
    compileOnly(libs.paperApi)
    paperLibrary(libs.jetty)
    paperLibrary(libs.simpleclientCommon)
    paperLibrary(libs.simpleclientHotspot)
}

description = "minecraft-prometheus-exporter"

tasks {
    // Include core classes in jar
    jar {
        from(project(":core").sourceSets.main.get().output)
    }
}

paper {
    main = "de.sldk.mc.PrometheusExporter"
    loader = "de.sldk.mc.PrometheusExporterLoader"
    generateLibrariesJson = true
    apiVersion = libs.versions.paperApi.get().replace(Regex("\\-R\\d.\\d-SNAPSHOT"), "")
    authors = listOf("Jim (AnEnragedPigeon)", "sldk")
}
