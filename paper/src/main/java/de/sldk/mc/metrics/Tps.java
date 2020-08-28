package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.tps.TpsCollector;
import io.prometheus.client.Gauge;
import org.bukkit.Bukkit;

public class Tps extends AbstractMetric {

    private static final Gauge TPS = Gauge.build()
            .name(prefix("tps"))
            .help("Server TPS (ticks per second)")
            .create();

    private int taskId;

    private TpsCollector tpsCollector = new TpsCollector();

    public Tps(PrometheusExporter plugin) {
        super(plugin, TPS);
    }

    @Override
    public void enable() {
        super.enable();
        this.taskId = startTask((PrometheusExporter) getPlugin());
    }

    @Override
    public void disable() {
        super.disable();
        Bukkit.getScheduler().cancelTask(taskId);
    }

    private int startTask(PrometheusExporter plugin) {
        return Bukkit.getServer()
                .getScheduler()
                .scheduleSyncRepeatingTask(plugin, tpsCollector, 0, TpsCollector.POLL_INTERVAL);
    }

    @Override
    public void doCollect() {
        TPS.set(tpsCollector.getAverageTPS());
    }
}
