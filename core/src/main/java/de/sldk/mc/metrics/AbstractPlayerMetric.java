package de.sldk.mc.metrics;

import io.prometheus.client.Collector;

public abstract class AbstractPlayerMetric<P> extends AbstractMetric {

    public AbstractPlayerMetric(Object plugin, Collector collector) {
        super(plugin, collector);
    }

    public abstract void doCollect();

    protected abstract void collect(P player);

    protected abstract String getUid(P player);

    protected abstract String getNameOrUid(P player);
}
