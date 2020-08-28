package de.sldk.mc.config;

import de.sldk.mc.core.config.AbstractPluginConfig;
import ninja.leaping.configurate.ConfigurationNode;

public class PluginConfig<T> extends AbstractPluginConfig<ConfigurationNode, T> {
    protected PluginConfig(String key, T defaultValue) {
        super(key, defaultValue);
    }

    public void setDefault(ConfigurationNode config) {
        if(config.getNode(this.key).isVirtual()) {
            config.getNode(this.key).setValue(this.defaultValue);
        }
    }

    @SuppressWarnings("unchecked")
    public T get(ConfigurationNode config) {
        return (T) config.getNode(this.key).getValue(this.defaultValue);
    }
}
