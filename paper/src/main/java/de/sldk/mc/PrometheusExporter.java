package de.sldk.mc;

import de.sldk.mc.core.ExporterPlugin;
import de.sldk.mc.config.ExporterConfig;
import de.sldk.mc.server.MetricsServer;
import org.bukkit.plugin.java.JavaPlugin;

public class PrometheusExporter extends JavaPlugin implements ExporterPlugin {
    private final ExporterConfig config = new ExporterConfig(this);
    private MetricsServer server;

    @Override
    public void onEnable() {
        config.load();

        config.enableConfiguredMetrics();

        server = new MetricsServer(this);
        server.startServer();
    }

    @Override
    public void onDisable() {
        server.stopServer();
        config.destroyMetrics();
    }

    @Override
    public ExporterConfig getExporterConfig() {
        return config;
    }
}
