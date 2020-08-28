package de.sldk.mc.config;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.metrics.AbstractMetric;

import java.util.function.Function;

public class MetricConfig extends PluginConfig<Boolean> {

    private static final String CONFIG_PATH_PREFIX = "enable_metrics";

    private final Function<PrometheusExporter, AbstractMetric> metricInitializer;

    protected MetricConfig(String key, Boolean defaultValue, Function<PrometheusExporter, AbstractMetric> metricInitializer) {
        super(CONFIG_PATH_PREFIX + "." + key, defaultValue);
        this.metricInitializer = metricInitializer;
    }

    public AbstractMetric getMetric(PrometheusExporter plugin) {
        return metricInitializer.apply(plugin);
    }
}
