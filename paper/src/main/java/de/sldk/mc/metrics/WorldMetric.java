package de.sldk.mc.metrics;

import io.prometheus.metrics.model.registry.Collector;
import org.bukkit.Bukkit;
import org.bukkit.World;

public abstract class WorldMetric extends AbstractMetric {

    public WorldMetric(Object plugin, Collector collector) {
        super(plugin, collector);
    }

    @Override
    protected final void initialValue() {
        for (World world : Bukkit.getWorlds()) {
            initialValue(world);
        }
    }

    protected abstract void initialValue(World world);
}
