package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.Util;
import io.prometheus.metrics.core.metrics.GaugeWithCallback;

import uk.co.notnull.platformdetection.Platform;
import uk.co.notnull.platformdetection.PlatformDetectionVelocity;

import java.util.Map;
import java.util.stream.Collectors;

public class PlayersOnlinePlatformTotal extends AbstractMetric {
    private static final GaugeWithCallback PLAYERS_ONLINE = GaugeWithCallback.builder()
            .name(prefix("players_online"))
            .help("Players currently online by server and version")
            .labelNames("server", "version", "client", "bedrock", "modded")
            .callback(callback -> Util.collectForServers(callback, PlayersOnlinePlatformTotal::collect))
            .build();

    public PlayersOnlinePlatformTotal(Object plugin) {
        super(plugin, PLAYERS_ONLINE);
    }

    @Override
    protected void initialValue() {
        PLAYERS_ONLINE.collect();
    }

    protected static void collect(GaugeWithCallback.Callback callback, RegisteredServer server) {
        PlatformDetectionVelocity platformDetection = (PlatformDetectionVelocity) (PrometheusExporter.getInstance().getPlatformDetection());

        Map<String, Map<Platform, Long>> collection = server.getPlayersConnected().stream().collect(
            Collectors.groupingBy(
                    (Player player) -> player.getProtocolVersion().getVersionIntroducedIn(), Collectors.groupingBy(
                            platformDetection::getPlatform, Collectors.counting()))
        );

        collection.forEach(
                (String version, Map<Platform, Long> clients) ->
                        clients.forEach((Platform platform, Long count) ->
                                                callback.call(count, server.getServerInfo().getName(),
                                                              version,
                                                              platform.getLabel(),
                                                              String.valueOf(platform.isBedrock()),
                                                              String.valueOf(platform.isModded()))));
    }
}
