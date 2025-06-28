package de.sldk.mc.config;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.core.config.AbstractPluginConfig;
import de.sldk.mc.metrics.AbstractMetric;
import de.sldk.mc.metrics.Entities;
import de.sldk.mc.metrics.LoadedChunks;
import de.sldk.mc.metrics.PlayersOnlineTotal;
import de.sldk.mc.metrics.PlayersTotal;
import de.sldk.mc.metrics.TickDurationAverageCollector;
import de.sldk.mc.metrics.TickDurationMaxCollector;
import de.sldk.mc.metrics.TickDurationMedianCollector;
import de.sldk.mc.metrics.TickDurationMinCollector;
import de.sldk.mc.metrics.Tps;
import de.sldk.mc.metrics.Villagers;
import io.prometheus.metrics.instrumentation.jvm.JvmMetrics;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ExporterConfig implements de.sldk.mc.core.config.ExporterConfig<FileConfiguration> {

    private final PluginConfig<String> host = new PluginConfig<>("host", "localhost");
    private final PluginConfig<Integer> port = new PluginConfig<>("port", 9225);
    private final List<MetricConfig> metrics = new ArrayList<>();

    private final PrometheusExporter plugin;

    public ExporterConfig(PrometheusExporter plugin) {
        this.plugin = plugin;

        metrics.add(metricConfig("entities", true, new Entities(plugin)));
        metrics.add(metricConfig("villagers", true, new Villagers(plugin)));
        metrics.add(metricConfig("loaded_chunks", true, new LoadedChunks(plugin)));
        metrics.add(metricConfig("players_online", true, new PlayersOnlineTotal(plugin)));
        metrics.add(metricConfig("players", false, new PlayersTotal(plugin)));
        metrics.add(metricConfig("tps", true, new Tps(plugin)));
        metrics.add(metricConfig("tick_duration_median", false, new TickDurationMedianCollector(plugin)));
        metrics.add(metricConfig("tick_duration_average", true, new TickDurationAverageCollector(plugin)));
        metrics.add(metricConfig("tick_duration_min", true, new TickDurationMinCollector(plugin)));
        metrics.add(metricConfig("tick_duration_max", true, new TickDurationMaxCollector(plugin)));
    }

    private static MetricConfig metricConfig(String key, boolean defaultValue, AbstractMetric metric) {
        return new MetricConfig(key, defaultValue, metric);
    }

    @Override
    public String getHost() {
        return get(host);
    }

    @Override
    public int getPort() {
        return get(port);
    }

    @Override
    public void load() {
        FileConfiguration configFile = plugin.getConfig();

        host.setDefault(configFile);
        port.setDefault(configFile);
        metrics.forEach(metric -> metric.setDefault(configFile));

        configFile.options().copyDefaults(true);
        save();
    }

    @Override
    public void save() {
        plugin.saveConfig();
    }

    public void destroyMetrics() {
        metrics.forEach(metricConfig -> {
            AbstractMetric metric = metricConfig.getMetric();

            if(metric.isEnabled()) {
                metric.disable();
                plugin.getLogger().info("AbstractMetric " + metric.getClass().getSimpleName() + " disabled");
            }
        });
    }

    public void enableConfiguredMetrics() {
        JvmMetrics.builder().register();

        metrics.forEach(metricConfig -> {
            AbstractMetric metric = metricConfig.getMetric();
            Boolean enabled = get(metricConfig);

            if (Boolean.TRUE.equals(enabled)) {
                metric.enable();
                plugin.getLogger().info("Metric " + metric.getClass().getSimpleName() + " enabled: " + enabled);
            }
        });
    }

    @Override
    public <T> T get(AbstractPluginConfig<FileConfiguration, T> config) {
        return config.get(plugin.getConfig());
    }
}
