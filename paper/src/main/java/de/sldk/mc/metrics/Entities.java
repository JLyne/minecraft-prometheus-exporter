package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class Entities extends WorldMetric {
    private static final Gauge ENTITIES = Gauge.build()
            .name(prefix("entities_total"))
            .help("Entities loaded per world")
            .labelNames("world", "alive", "spawnable", "type")
            .create();

    public Entities(PrometheusExporter plugin) {
        super(plugin, ENTITIES);
    }

    @Override
    public void collect(World world) {
        Map<EntityType, Integer> counts = new HashMap<>();

        for (Entity entity : world.getEntities()) {
            counts.compute(entity.getType(), (EntityType type, Integer value) -> value != null ? value + 1 : 1);
        }

        counts.forEach((EntityType type, Integer value) -> {
            ENTITIES.labels(world.getName(), String.valueOf(type.isAlive()),
                            String.valueOf(type.isSpawnable()), type.getKey().getKey()).set(value);
        });
    }
}
