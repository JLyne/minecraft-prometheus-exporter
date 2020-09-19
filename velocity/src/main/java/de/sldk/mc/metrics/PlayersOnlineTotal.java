package de.sldk.mc.metrics;

import com.techjar.vbe.VivecraftAPI;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.geysermc.floodgate.FloodgateAPI;

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
        Map<String, Map<String, Long>> collection = server.getPlayersConnected().stream().collect(
            Collectors.groupingBy((Player player) -> player.getProtocolVersion().getName(), Collectors.groupingBy((Player player) -> {
                if(PrometheusExporter.getInstance().isVivecraftEnabled() && VivecraftAPI.isVive(player)) {
                    return VivecraftAPI.isVR(player) ? "vivecraft" : "vivecraft-novr";
                }

                if (PrometheusExporter.getInstance().isFloodgateEnabled() && FloodgateAPI.isBedrockPlayer(player)) {
                    return "bedrock";
                } else {
                    return "vanilla";
                }
            }, Collectors.counting()))
        );

        PrometheusExporter.getInstance().getLogger().info(collection.toString());

        collection.forEach((String version, Map<String, Long> clients) -> {
            clients.forEach((String client, Long count) -> {
                PLAYERS_ONLINE.labels(server.getServerInfo().getName(), version, client).set(count);
            });
        });
    }

    @Override
    protected void clear() {
        PLAYERS_ONLINE.clear();
    }
}
