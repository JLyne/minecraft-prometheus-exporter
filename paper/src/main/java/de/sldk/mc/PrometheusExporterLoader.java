package de.sldk.mc;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class PrometheusExporterLoader implements PluginLoader {

	@Override
	public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
		MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addDependency(new Dependency(new DefaultArtifact("@jettyDependency@"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("@simpleclientCommonDependency@"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("@simpleclientHotspotDependency@"), null));

        resolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());

        classpathBuilder.addLibrary(resolver);
	}
}
