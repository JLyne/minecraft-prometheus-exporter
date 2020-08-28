package de.sldk.mc.metrics;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Collector;

public abstract class ServerMetric extends AbstractMetric {

    public ServerMetric(Object plugin, Collector collector) {
        super(plugin, collector);
    }

    @Override
    public final void doCollect() {
        for (RegisteredServer server : PrometheusExporter.getInstance().getProxy().getAllServers()) {
            collect(server);
        }
    }

    protected abstract void collect(RegisteredServer server);

}
