package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.sldk.mc.Util;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;

import java.util.Map;
import java.util.stream.Collectors;

public class PlayersOnlineTotal extends AbstractMetric {
    private static final GaugeWithCallback PLAYERS_ONLINE = GaugeWithCallback.builder()
            .name(prefix("players_online_total"))
            .help("Players currently online by server and version")
            .labelNames("server", "version", "client")
            .callback(callback -> Util.collectForServers(callback, PlayersOnlineTotal::collect))
            .build();

    public PlayersOnlineTotal(Object plugin) {
        super(plugin, PLAYERS_ONLINE);
    }

    @Override
    protected void initialValue() {
        PLAYERS_ONLINE.collect();
    }

    protected static void collect(GaugeWithCallback.Callback callback, RegisteredServer server) {
        getCollectedPlayers(server).forEach((String version, Long count) ->
                                   callback.call(count, server.getServerInfo().getName(), version));
    }

    protected static Map<String, Long> getCollectedPlayers(RegisteredServer server) {
        return server.getPlayersConnected().stream().collect(
            Collectors.groupingBy((Player player) ->
                                          player.getProtocolVersion().getVersionIntroducedIn(), Collectors.counting())
        );
    }
}
