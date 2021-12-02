package de.sldk.mc.config;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.core.MetricRegistry;
import de.sldk.mc.core.config.AbstractPluginConfig;
import de.sldk.mc.metrics.AbstractMetric;
import de.sldk.mc.metrics.*;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public class ExporterConfig implements de.sldk.mc.core.config.ExporterConfig<FileConfiguration> {

    private final PluginConfig<String> host = new PluginConfig<>("host", "localhost");
    private final PluginConfig<Integer> port = new PluginConfig<>("port", 9225);
    private final List<MetricConfig> metrics = Arrays.asList(
            metricConfig("entities_total", true, Entities::new),
            metricConfig("villagers_total", true, Villagers::new),
            metricConfig("loaded_chunks_total", true, LoadedChunks::new),
            metricConfig("jvm_memory", true, Memory::new),
            metricConfig("players_online_total", true, PlayersOnlineTotal::new),
            metricConfig("players_total", true, PlayersTotal::new),
            metricConfig("tps", true, Tps::new),

            metricConfig("jvm_threads", true, Threads::new),
            metricConfig("jvm_gc", true, GarbageCollection::new),

            metricConfig("tick_duration_median", true, TickDurationMedianCollector::new),
            metricConfig("tick_duration_average", true, TickDurationAverageCollector::new),
            metricConfig("tick_duration_min", false, TickDurationMinCollector::new),
            metricConfig("tick_duration_max", true, TickDurationMaxCollector::new),

            metricConfig("player_online", false, PlayerOnline::new),
            metricConfig("player_statistic", false, PlayerStatistics::new));

    private final PrometheusExporter plugin;

    private final HashSet<AbstractMetric> registeredMetrics = new HashSet<>();

    public ExporterConfig(PrometheusExporter plugin) {
        this.plugin = plugin;
    }

    private static MetricConfig metricConfig(String key, boolean defaultValue, Function<PrometheusExporter, AbstractMetric> metricInitializer) {
        return new MetricConfig(key, defaultValue, metricInitializer);
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
        registeredMetrics.forEach(metric -> {
            if(metric.isEnabled()) {
                metric.disable();
                plugin.getLogger().fine("AbstractMetric " + metric.getClass().getSimpleName() + " disabled");
            }

            MetricRegistry.getInstance().unregister(metric);
        });

        registeredMetrics.clear();
    }

    public void enableConfiguredMetrics() {
        metrics.forEach(metricConfig -> {
            AbstractMetric metric = metricConfig.getMetric(plugin);
            Boolean enabled = get(metricConfig);

            if (Boolean.TRUE.equals(enabled)) {
                metric.enable();
                plugin.getLogger().fine("Metric " + metric.getClass().getSimpleName() + " enabled: " + enabled);
            }

            MetricRegistry.getInstance().register(metric);
            registeredMetrics.add(metric);
        });
    }

    @Override
    public <T> T get(AbstractPluginConfig<FileConfiguration, T> config) {
        return config.get(plugin.getConfig());
    }
}
