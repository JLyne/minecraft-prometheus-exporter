package de.sldk.mc;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Properties;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class PrometheusExporterLoader implements PluginLoader {

	@Override
	public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
		MavenLibraryResolver resolver = new MavenLibraryResolver();

		Properties properties = new Properties();
		try {
			properties.load(PrometheusExporterLoader.class.getResourceAsStream("/plugin.properties"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String jetty = properties.getProperty("jettyDependency");
		String simpleClientCommon = properties.getProperty("simpleclientCommonDependency");
		String simpleClientHotspot = properties.getProperty("simpleclientHotspotDependency");

        resolver.addDependency(new Dependency(new DefaultArtifact(jetty), null));
        resolver.addDependency(new Dependency(new DefaultArtifact(simpleClientCommon), null));
        resolver.addDependency(new Dependency(new DefaultArtifact(simpleClientHotspot), null));

        resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());

        classpathBuilder.addLibrary(resolver);
	}
}
