package de.sldk.mc.core.config;

public interface ExporterConfig<C> {
    String getHost();
    int getPort();

    void load();
    void save();
    void enableConfiguredMetrics();

    <T> T get(AbstractPluginConfig<C, T> config);
}
