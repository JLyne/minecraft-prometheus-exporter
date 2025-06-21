package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import org.bukkit.Bukkit;

public class PlayersTotal extends AbstractMetric {
    private static final Gauge PLAYERS = Gauge.builder()
            .name(prefix("players"))
            .help("Unique players (online + offline)")
            .build();

    public PlayersTotal(PrometheusExporter plugin) {
        super(plugin, PLAYERS);
    }

    protected void initialValue() {
        PLAYERS.set(Bukkit.getOfflinePlayers().length);
    }

    public static void addPlayer() {
        PLAYERS.labelValues().inc();
    }
}
