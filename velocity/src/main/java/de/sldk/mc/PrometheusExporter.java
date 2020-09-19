package de.sldk.mc;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import de.sldk.mc.core.ExporterPlugin;
import de.sldk.mc.config.ExporterConfig;
import de.sldk.mc.server.MetricsServer;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

@Plugin(id="velocity-prometheus-exporter", name="Velocity Prometheus Exporter",
        version="1.0-SNAPSHOT", authors = { "Jim" }, dependencies = {
        @Dependency(id="floodgate", optional = true),
        @Dependency(id="vivecraft-velocity-extensions", optional = true)
})
public class PrometheusExporter implements ExporterPlugin {
    private final ExporterConfig config = new ExporterConfig(this);
    private static PrometheusExporter instance;
    private MetricsServer server;

    private final ProxyServer proxy;
    private final Logger logger;
    private boolean floodgateEnabled = false;
    private boolean vivecraftEnabled = false;

    @Inject
    @DataDirectory
    private Path dataDirectory;

    @Inject
    public PrometheusExporter(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
        instance = this;
    }

    @Subscribe
    public void onProxyInitialized(ProxyInitializeEvent event) {
       init();
    }

    public void onProxyReload(ProxyReloadEvent event) {
        server.stopServer();
        init();
    }

    private void init() {
        Optional<PluginContainer> floodgate = proxy.getPluginManager().getPlugin("floodgate");
        floodgateEnabled = floodgate.isPresent();

        Optional<PluginContainer> vivecraft = proxy.getPluginManager().getPlugin("vivecraft-velocity-extensions");
        vivecraftEnabled = vivecraft.isPresent();

        config.load();
        config.enableConfiguredMetrics();

        server = new MetricsServer(this);
        server.startServer();
    }

    @Override
    public ExporterConfig getExporterConfig() {
        return config;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public static PrometheusExporter getInstance() {
        return instance;
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public boolean isFloodgateEnabled() {
        return floodgateEnabled;
    }

	public boolean isVivecraftEnabled() {
        return vivecraftEnabled;
	}
}
