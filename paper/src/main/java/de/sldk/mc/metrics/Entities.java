package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public class Entities extends WorldMetric {
    private static final Gauge ENTITIES = Gauge.builder()
            .name(prefix("entities"))
            .help("Entities loaded per world")
            .labelNames("world", "alive", "spawnable", "type")
            .build();

    public Entities(PrometheusExporter plugin) {
        super(plugin, ENTITIES);
    }

    @Override
    protected void initialValue(World world) {
        Map<EntityType, Integer> counts = new HashMap<>();

        for (Entity entity : world.getEntities()) {
            counts.compute(entity.getType(), (EntityType type, Integer value) -> value != null ? value + 1 : 1);
        }

        counts.forEach(
                (EntityType type, Integer value) -> ENTITIES.labelValues(world.getName(), String.valueOf(type.isAlive()),
                                                                    String.valueOf(type.isSpawnable()),
                                                                    type.getKey().getKey()).set(value));
    }

    public static void addEntity(EntityType type, World world) {
        ENTITIES.labelValues(world.getName(),
                                      String.valueOf(type.isAlive()),
                                      String.valueOf(type.isSpawnable()), type.getKey().getKey()).inc();
    }

    public static void removeEntity(EntityType type, World world) {
        ENTITIES.labelValues(world.getName(),
                                      String.valueOf(type.isAlive()),
                                      String.valueOf(type.isSpawnable()), type.getKey().getKey()).dec();
    }
}
