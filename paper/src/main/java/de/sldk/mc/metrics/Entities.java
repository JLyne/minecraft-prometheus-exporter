package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.bukkit.World;

public class Entities extends WorldMetric {
    private static final Gauge ENTITIES = Gauge.build()
            .name(prefix("entities_total"))
            .help("Entities loaded per world")
            .labelNames("world")
            .create();

    public Entities(PrometheusExporter plugin) {
        super(plugin, ENTITIES);
    }

    @Override
    public void collect(World world) {
        ENTITIES.labels(world.getName()).set(world.getEntities().size());
    }
}
