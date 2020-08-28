package de.sldk.mc.config;

import de.sldk.mc.core.config.AbstractPluginConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class PluginConfig<T> extends AbstractPluginConfig<FileConfiguration, T> {
    protected PluginConfig(String key, T defaultValue) {
        super(key, defaultValue);
    }

    public void setDefault(FileConfiguration config) {
        config.addDefault(this.key, this.defaultValue);
    }

    @SuppressWarnings("unchecked")
    public T get(FileConfiguration config) {
        return (T) config.get(this.key);
    }
}
