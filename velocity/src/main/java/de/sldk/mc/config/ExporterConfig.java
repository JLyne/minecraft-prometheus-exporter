package de.sldk.mc.config;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.core.MetricRegistry;
import de.sldk.mc.core.config.AbstractPluginConfig;
import de.sldk.mc.metrics.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ExporterConfig implements de.sldk.mc.core.config.ExporterConfig<ConfigurationNode> {

    private final PluginConfig<String> host = new PluginConfig<>("host", "localhost");
    private final PluginConfig<Integer> port = new PluginConfig<>("port", 9225);
    private final List<MetricConfig> metrics = Arrays.asList(
            metricConfig("jvm_memory", true, Memory::new),
            metricConfig("players_online_total", true, PlayersOnlineTotal::new),
            metricConfig("players_total", true, PlayersTotal::new),
            metricConfig("player_online", false, PlayerOnline::new));

    private final PrometheusExporter plugin;
    private ConfigurationNode config;

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

    public void load() {
        try {
            File configFile = new File(plugin.getDataDirectory().toFile(), "config.yml");

            if (!configFile.exists()) {
                File parent = configFile.getParentFile();

                if(!parent.exists()) {
                    parent.mkdirs();
                }
            }

            config = YAMLConfigurationLoader.builder().setIndent(2)
                    .setPath(configFile.toPath()).build().load();
        } catch (IOException e) {
            e.printStackTrace();
            config = ConfigurationNode.root();
        }

        host.setDefault(config);
        port.setDefault(config);
        metrics.forEach(metric -> metric.setDefault(config));

        save();
    }

    public void save() {
        try {
            YAMLConfigurationLoader.builder().setIndent(2)
                        .setPath(new File(plugin.getDataDirectory().toFile(), "config.yml").toPath())
                    .build().save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enableConfiguredMetrics() {
        metrics.forEach(metricConfig -> {
            AbstractMetric metric = metricConfig.getMetric(plugin);
            Boolean enabled = get(metricConfig);

            if (Boolean.TRUE.equals(enabled)) {
                metric.enable();
            }

            plugin.getLogger().fine("AbstractMetric " + metric.getClass().getSimpleName() + " enabled: " + enabled);

            MetricRegistry.getInstance().register(metric);
        });
    }

    @Override
    public <T> T get(AbstractPluginConfig<ConfigurationNode, T> config) {
        return config.get(this.config);
    }
}
