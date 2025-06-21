package de.sldk.mc.metrics;

import io.prometheus.metrics.model.registry.Collector;
import io.prometheus.metrics.model.registry.PrometheusRegistry;

@SuppressWarnings("unused")
public abstract class AbstractMetric {
    protected final static String COMMON_PREFIX = "mc_";

    protected final Object plugin;
    protected final Collector collector;

    protected boolean enabled = false;

    protected AbstractMetric(Object plugin, Collector metric) {
        this.plugin = plugin;
        this.collector = metric;
        initialValue();
    }

    protected Object getPlugin() {
        return plugin;
    }

    protected static String prefix(String name) {
        return COMMON_PREFIX + name;
    }

    public void enable() {
        PrometheusRegistry.defaultRegistry.register(collector);
        System.out.println(PrometheusRegistry.defaultRegistry);
        enabled = true;
    }

    public void disable() {
        if(enabled) {
            PrometheusRegistry.defaultRegistry.unregister(collector);
            enabled = false;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected abstract void initialValue();
}
