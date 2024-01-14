package de.sldk.mc.config;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.core.MetricRegistry;
import de.sldk.mc.core.config.AbstractPluginConfig;
import de.sldk.mc.metrics.*;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public class ExporterConfig implements de.sldk.mc.core.config.ExporterConfig<ConfigurationNode> {

    private final PluginConfig<String> host = new PluginConfig<>("host", "localhost");
    private final PluginConfig<Integer> port = new PluginConfig<>("port", 9225);
    private final List<MetricConfig> metrics = new ArrayList<>(Arrays.asList(
            metricConfig("jvm_memory", true, Memory::new),
            metricConfig("jvm_threads", true, Threads::new),
            metricConfig("jvm_gc", true, GarbageCollection::new),
            metricConfig("players_total", true, PlayersTotal::new),
            metricConfig("player_online", false, PlayerOnline::new)));

    private final HashSet<AbstractMetric> registeredMetrics = new HashSet<>();

    private final PrometheusExporter plugin;
    private ConfigurationNode config;

    public ExporterConfig(PrometheusExporter plugin) {
        this.plugin = plugin;

        if(plugin.isPlatformDetectionEnabled()) {
            metrics.add(metricConfig("players_online_total", true, PlayersOnlinePlatformTotal::new));
        } else {
            metrics.add(metricConfig("players_online_total", true, PlayersOnlineTotal::new));
        }
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

    public void load() {
        try {
            File configFile = new File(plugin.getDataDirectory().toFile(), "config.yml");

            if (!configFile.exists()) {
                File parent = configFile.getParentFile();

                if(!parent.exists()) {
                    parent.mkdirs();
                }
            }

            config = YamlConfigurationLoader.builder().indent(2).path(configFile.toPath()).build().load();
        } catch (IOException e) {
            e.printStackTrace();
            config = CommentedConfigurationNode.root();
        }

        host.setDefault(config);
        port.setDefault(config);
        metrics.forEach(metric -> metric.setDefault(config));

        save();
    }

    public void save() {
        try {
            YamlConfigurationLoader.builder().indent(2)
                    .path(new File(plugin.getDataDirectory().toFile(), "config.yml").toPath())
                    .build().save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public <T> T get(AbstractPluginConfig<ConfigurationNode, T> config) {
        return config.get(this.config);
    }
}
