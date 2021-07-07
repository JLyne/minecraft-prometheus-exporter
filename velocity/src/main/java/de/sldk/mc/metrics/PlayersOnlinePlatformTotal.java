package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import uk.co.notnull.platformdetection.Platform;
import uk.co.notnull.platformdetection.PlatformDetectionVelocity;

import java.util.Map;
import java.util.stream.Collectors;

public class PlayersOnlinePlatformTotal extends ServerMetric {
    private final PlatformDetectionVelocity platformDetection;

    private static final Gauge PLAYERS_ONLINE = Gauge.build()
            .name(prefix("players_online_total"))
            .help("Players currently online by server and version")
            .labelNames("server", "version", "client", "bedrock", "modded")
            .create();

    public PlayersOnlinePlatformTotal(Object plugin) {
        super(plugin, PLAYERS_ONLINE);
        platformDetection = (PlatformDetectionVelocity) ((PrometheusExporter) plugin).getPlatformDetection();
    }

    @Override
    protected void collect(RegisteredServer server) {
        Map<String, Map<Platform, Long>> collection = server.getPlayersConnected().stream().collect(
            Collectors.groupingBy(
                    (Player player) -> player.getProtocolVersion().getVersionIntroducedIn(), Collectors.groupingBy(
                            platformDetection::getPlatform, Collectors.counting()))
        );

        collection.forEach(
                (String version, Map<Platform, Long> clients) ->
                        clients.forEach((Platform platform, Long count) ->
                                                PLAYERS_ONLINE.labels(server.getServerInfo().getName(),
                                                                      version,
                                                                      platform.getLabel(),
                                                                      String.valueOf(platform.isBedrock()),
                                                                      String.valueOf(platform.isModded()))
                                                        .set(count)));
    }

    @Override
    protected void clear() {
        PLAYERS_ONLINE.clear();
    }
}
