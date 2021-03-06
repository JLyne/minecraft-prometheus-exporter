package de.sldk.mc.metrics;

import io.prometheus.client.Collector;
import org.bukkit.Bukkit;
import org.bukkit.World;

public abstract class WorldMetric extends AbstractMetric {

    public WorldMetric(Object plugin, Collector collector) {
        super(plugin, collector);
    }

    @Override
    public final void doCollect() {
        for (World world : Bukkit.getWorlds()) {
            collect(world);
        }
    }

    protected abstract void collect(World world);

}
