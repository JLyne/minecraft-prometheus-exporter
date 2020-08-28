package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;

public class PlayersTotal extends AbstractMetric {

    private static final Gauge PLAYERS = Gauge.build()
            .name(prefix("players_total"))
            .help("Unique players (online + offline)")
            .create();

    public PlayersTotal(Object plugin) {
        super(plugin, PLAYERS);
    }

    @Override
    public void doCollect() {
        PLAYERS.set(PrometheusExporter.getInstance().getProxy().getAllPlayers().size());
    }
}
