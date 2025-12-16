package de.sldk.mc.config;

import de.sldk.mc.core.config.AbstractPluginConfig;
import io.leangen.geantyref.TypeToken;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public class PluginConfig<T> extends AbstractPluginConfig<ConfigurationNode, T> {
    private final String[] key;

    protected PluginConfig(String key, T defaultValue) {
        super(key, defaultValue);
        this.key = new String[] {key};
    }

    protected PluginConfig(String[] key, T defaultValue) {
        super(String.join(".", key), defaultValue);
        this.key = key;
    }

    public void setDefault(ConfigurationNode config) {
        if(config.node((Object[]) key).virtual()) {
            try {
                config.node((Object[]) key).set(this.defaultValue);
            } catch (SerializationException ignored) {}
        }
    }

    @SuppressWarnings("unchecked")
    public T get(ConfigurationNode config) {
        try {
            return (T) config.node((Object[]) key).get(TypeToken.get(Object.class), defaultValue);

        } catch (SerializationException e) {
            return defaultValue;
        }
    }
}
