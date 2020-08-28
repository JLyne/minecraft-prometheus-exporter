package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;

public class PlayerOnline extends PlayerMetric {

    private static final Gauge PLAYERS_WITH_NAMES = Gauge.build()
            .name(prefix("player_online"))
            .help("Online state by player name")
            .labelNames("name", "uid")
            .create();

    public PlayerOnline(PrometheusExporter plugin) {
        super(plugin, PLAYERS_WITH_NAMES);
    }

    @Override
    public void collect(Player player) {
        PLAYERS_WITH_NAMES.labels(getNameOrUid(player), getUid(player)).set(player.isActive() ? 1 : 0);
    }
}
