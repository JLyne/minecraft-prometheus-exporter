package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.bukkit.World;

public class LivingEntities extends WorldMetric {

    private static final Gauge LIVING_ENTITIES = Gauge.build()
            .name(prefix("living_entities_total"))
            .help("Living entities loaded per world")
            .labelNames("world")
            .create();

    public LivingEntities(PrometheusExporter plugin) {
        super(plugin, LIVING_ENTITIES);
    }

    @Override
    protected void collect(World world) {
        LIVING_ENTITIES.labels(world.getName()).set(world.getLivingEntities().size());
    }
}
