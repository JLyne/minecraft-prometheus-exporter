package de.sldk.mc.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;

@SuppressWarnings("unused")
public abstract class AbstractMetric {
    protected final static String COMMON_PREFIX = "mc_";

    protected final Object plugin;
    protected final Collector collector;

    protected boolean enabled = false;

    protected AbstractMetric(Object plugin, Collector collector) {
        this.plugin = plugin;
        this.collector = collector;
    }

    protected Object getPlugin() {
        return plugin;
    }

    public void collect() {
        if (enabled) {
            doCollect();
        }
    }

    protected abstract void doCollect();

    protected static String prefix(String name) {
        return COMMON_PREFIX + name;
    }

    public void enable() {
        CollectorRegistry.defaultRegistry.register(collector);
        enabled = true;
    }

    public void disable() {
        if(enabled) {
            CollectorRegistry.defaultRegistry.unregister(collector);
            enabled = false;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
