package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import org.bukkit.Bukkit;

public class Tps extends AbstractMetric {
    private static final Gauge TPS = Gauge.builder()
            .name(prefix("tps"))
            .help("Server TPS (ticks per second)")
            .build();

    public Tps(PrometheusExporter plugin) {
        super(plugin, TPS);
    }

    protected void initialValue() {
        collect();
    }

    public static void collect() {
        TPS.set(Bukkit.getServer().getTPS()[0]);
    }
}
