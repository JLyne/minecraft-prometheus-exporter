package de.sldk.mc.metrics;

import de.sldk.mc.core.ExporterPlugin;
import io.prometheus.client.Gauge;

public class Memory extends AbstractMetric {

    private static final Gauge MEMORY = Gauge.build()
            .name(prefix("jvm_memory"))
            .help("JVM memory usage")
            .labelNames("type")
            .create();

    public Memory(ExporterPlugin plugin) {
        super(plugin, MEMORY);
    }

    @Override
    public void doCollect() {
        MEMORY.labels("max").set(Runtime.getRuntime().maxMemory());
        MEMORY.labels("free").set(Runtime.getRuntime().freeMemory());
    }
}
