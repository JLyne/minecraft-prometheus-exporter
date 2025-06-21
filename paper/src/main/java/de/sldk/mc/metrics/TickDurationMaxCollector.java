package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;

public class TickDurationMaxCollector extends TickDurationCollector {
    private static final Gauge TD = Gauge.builder()
            .name(prefix("tick_duration_max"))
            .help("Max duration of server tick")
            .unit(Unit.SECONDS)
            .build();

    public TickDurationMaxCollector(PrometheusExporter plugin) {
        super(plugin, TD);
    }

    private static long getTickDurationMax() {
        long max = Long.MIN_VALUE;
        for (Long val : getTickDurations()) {
            if (val > max) {
                max = val;
            }
        }
        return max;
    }

    protected void initialValue() {
        super.initialValue();
        collect();
    }

    public static void collect() {
        TD.set(Unit.nanosToSeconds(getTickDurationMax()));
    }
}

