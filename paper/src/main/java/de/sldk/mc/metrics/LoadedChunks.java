package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import org.bukkit.World;

public class LoadedChunks extends WorldMetric {
    private static final Gauge LOADED_CHUNKS = Gauge.builder()
            .name(prefix("loaded_chunks"))
            .help("Chunks loaded per world")
            .labelNames("world")
            .build();

    public LoadedChunks(PrometheusExporter plugin) {
        super(plugin, LOADED_CHUNKS);
    }

    @Override
    protected void initialValue(World world) {
        LOADED_CHUNKS.labelValues(world.getName()).set(world.getLoadedChunks().length);
    }

    public static void addChunk(World world) {
        LOADED_CHUNKS.labelValues(world.getName()).inc();
    }

    public static void removeChunk(World world) {
        LOADED_CHUNKS.labelValues(world.getName()).dec();
    }
}
