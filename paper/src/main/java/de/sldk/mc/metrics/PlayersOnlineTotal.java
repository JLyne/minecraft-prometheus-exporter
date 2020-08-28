package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.bukkit.World;

public class PlayersOnlineTotal extends WorldMetric {

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online per world")
            .labelNames("world")
            .create();

    public PlayersOnlineTotal(PrometheusExporter plugin) {
        super(plugin, PLAYERS_ONLINE);
    }

    @Override
    protected void collect(World world) {
        PLAYERS_ONLINE.labels(world.getName()).set(world.getPlayers().size());
    }
}
