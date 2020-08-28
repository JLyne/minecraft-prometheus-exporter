package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.client.Gauge;
import org.bukkit.World;

public class LoadedChunks extends WorldMetric {

    private static final Gauge LOADED_CHUNKS = Gauge.build()
            .name(prefix("loaded_chunks_total"))
            .help("Chunks loaded per world")
            .labelNames("world")
            .create();

    public LoadedChunks(PrometheusExporter plugin) {
        super(plugin, LOADED_CHUNKS);
    }

    @Override
    public void collect(World world) {
        LOADED_CHUNKS.labels(world.getName()).set(world.getLoadedChunks().length);
    }
}
