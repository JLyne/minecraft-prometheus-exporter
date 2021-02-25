package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import uk.co.notnull.platformdetection.Platform;
import uk.co.notnull.platformdetection.PlatformDetectionVelocity;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayersOnlinePlatformTotal extends ServerMetric {
    private final PlatformDetectionVelocity platformDetection;

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online by server and version")
            .labelNames("server", "version", "client")
            .create();

    public PlayersOnlinePlatformTotal(Object plugin) {
        super(plugin, PLAYERS_ONLINE);
        platformDetection = ((PrometheusExporter) plugin).getPlatformDetection();
    }

    @Override
    protected void collect(RegisteredServer server) {
        Map<String, Map<String, Long>> collection = server.getPlayersConnected().stream().collect(
            Collectors.groupingBy((Player player) -> player.getProtocolVersion().getName(), Collectors.groupingBy((Player player) -> {
                Platform platform = platformDetection.getPlatform(player);

                if(platform.isBedrock()) {
                    return "bedrock";
                }

                return platform.getLabel().toLowerCase(Locale.ROOT).replace(" ", "");
            }, Collectors.counting()))
        );

        PrometheusExporter.getInstance().getLogger().info(collection.toString());

        collection.forEach((String version, Map<String, Long> clients) -> {
            clients.forEach((String client, Long count) -> {
                PLAYERS_ONLINE.labels(server.getServerInfo().getName(), client).set(count);
            });
        });
    }

    @Override
    protected void clear() {
        PLAYERS_ONLINE.clear();
    }
}
