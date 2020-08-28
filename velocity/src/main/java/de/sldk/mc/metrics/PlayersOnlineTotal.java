package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.geysermc.floodgate.FloodgateAPI;

import java.util.stream.Collectors;

public class PlayersOnlineTotal extends ServerMetric {

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online by server and version")
            .labelNames("server", "version")
            .create();

    public PlayersOnlineTotal(Object plugin) {
        super(plugin, PLAYERS_ONLINE);
    }

    @Override
    protected void collect(RegisteredServer server) {
        server.getPlayersConnected().stream().collect(
            Collectors.groupingBy((Player player) -> {
                if (PrometheusExporter.getInstance().isFloodgateEnabled() && FloodgateAPI.isBedrockPlayer(player)) {
                    return "bedrock";
                } else {
                    return player.getProtocolVersion().getName();
                }
            }, Collectors.counting())).forEach((String version, Long count) -> {
            PLAYERS_ONLINE.labels(server.getServerInfo().getName(), version).set(count);
        });
    }
}
