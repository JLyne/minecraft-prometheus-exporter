package de.sldk.mc.server;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.core.server.AbstractServer;
import org.bukkit.Bukkit;

import java.util.concurrent.Future;

public class MetricsServer extends AbstractServer {
    public MetricsServer(PrometheusExporter exporter) {
        super(exporter);
    }

    protected Future<Object> collectMetrics() {
        /*
         * Bukkit API calls have to be made from the main thread.
         * That's why we use the BukkitScheduler to retrieve the server stats.
         * */
        return Bukkit.getServer().getScheduler().callSyncMethod((PrometheusExporter) exporter, () -> {
            metricRegistry.collectMetrics();
            return null;
        });
    }
}
