package de.sldk.mc.config;

import de.sldk.mc.metrics.AbstractMetric;

public class MetricConfig extends PluginConfig<Boolean> {

    private static final String CONFIG_PATH_PREFIX = "enable_metrics";

    private final AbstractMetric metric;

    protected MetricConfig(String key, Boolean defaultValue, AbstractMetric metric) {
        super(CONFIG_PATH_PREFIX + "." + key, defaultValue);
        this.metric = metric;
    }

    public AbstractMetric getMetric() {
        return metric;
    }
}
