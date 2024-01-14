package de.sldk.mc.config;

import de.sldk.mc.core.config.AbstractPluginConfig;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class PluginConfig<T> extends AbstractPluginConfig<ConfigurationNode, T> {
    protected PluginConfig(String key, T defaultValue) {
        super(key, defaultValue);
    }

    public void setDefault(ConfigurationNode config) {
        if(config.node(this.key).virtual()) {
            try {
                config.node(this.key).set(this.defaultValue);
            } catch (SerializationException ignored) {}
        }
    }

    @SuppressWarnings("unchecked")
    public T get(ConfigurationNode config) {
        try {
            return (T) config.node(this.key).get(new TypeToken<T>(){}.getType(), defaultValue);

        } catch (SerializationException e) {
            return defaultValue;
        }
    }
}
