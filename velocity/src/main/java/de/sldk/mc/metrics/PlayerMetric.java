package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.Player;
import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Collector;

public abstract class PlayerMetric extends AbstractPlayerMetric<Player> {

    public PlayerMetric(PrometheusExporter plugin, Collector collector) {
        super(plugin, collector);
    }

    @Override
    public final void doCollect() {
        for (Player player : PrometheusExporter.getInstance().getProxy().getAllPlayers()) {
            collect(player);
        }
    }

    protected abstract void collect(Player player);

    protected String getUid(Player player) {
        return player.getUniqueId().toString();
    }

    protected String getNameOrUid(Player player) {
        return player.getUsername() != null ? player.getUsername() : player.getUniqueId().toString();
    }

}
