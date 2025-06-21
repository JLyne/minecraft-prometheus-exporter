package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import org.bukkit.World;

public class PlayersOnlineTotal extends WorldMetric {
    private static final Gauge PLAYERS_ONLINE = Gauge.builder()
            .name(prefix("players_online"))
            .help("Players currently online per world")
            .labelNames("world")
            .build();

    public PlayersOnlineTotal(PrometheusExporter plugin) {
        super(plugin, PLAYERS_ONLINE);
    }

    @Override
    protected void initialValue(World world) {
        PLAYERS_ONLINE.labelValues(world.getName()).set(world.getPlayers().size());
    }

    public static void addPlayer(World world) {
        PLAYERS_ONLINE.labelValues(world.getName()).inc();
    }

    public static void removePlayer(World world) {
        PLAYERS_ONLINE.labelValues(world.getName()).dec();
    }
}
