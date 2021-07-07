package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.bukkit.Bukkit;

public class Tps extends AbstractMetric {

    private static final Gauge TPS = Gauge.build()
            .name(prefix("tps"))
            .help("Server TPS (ticks per second)")
            .create();

    public Tps(PrometheusExporter plugin) {
        super(plugin, TPS);
    }

    @Override
    public void enable() {
        super.enable();
    }

    @Override
    public void disable() {
        super.disable();
    }

    @Override
    public void doCollect() {
        TPS.set(Bukkit.getServer().getTPS()[0]);
    }
}
