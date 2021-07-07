package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.prometheus.client.Gauge;

import java.util.Map;
import java.util.stream.Collectors;

public class PlayersOnlineTotal extends ServerMetric {

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online by server and version")
            .labelNames("server", "version", "client")
            .create();

    public PlayersOnlineTotal(Object plugin) {
        super(plugin, PLAYERS_ONLINE);
    }

    @Override
    protected void collect(RegisteredServer server) {
        Map<String, Long> collection = server.getPlayersConnected().stream().collect(
            Collectors.groupingBy((Player player) ->
                                          player.getProtocolVersion().getVersionIntroducedIn(), Collectors.counting())
        );

        collection.forEach((String version, Long count) ->
                                   PLAYERS_ONLINE.labels(server.getServerInfo().getName(), version).set(count));
    }

    @Override
    protected void clear() {
        PLAYERS_ONLINE.clear();
    }
}
