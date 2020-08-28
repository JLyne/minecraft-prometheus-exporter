package de.sldk.mc.core.config;

public abstract class AbstractPluginConfig<C, T> {
    protected final String key;
    protected final T defaultValue;

    protected AbstractPluginConfig(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public abstract void setDefault(C config);

    public abstract T get(C config);
}
