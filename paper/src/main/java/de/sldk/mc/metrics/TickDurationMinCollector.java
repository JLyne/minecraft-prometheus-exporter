package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;

public class TickDurationMinCollector extends TickDurationCollector {
    private static final Gauge TD = Gauge.builder()
            .name(prefix("tick_duration_min"))
            .help("Min duration of server tick")
            .unit(Unit.SECONDS)
            .build();

    public TickDurationMinCollector(PrometheusExporter plugin) {
        super(plugin, TD);
    }

    private static long getTickDurationMin() {
        long min = Long.MAX_VALUE;
        for (Long val : getTickDurations()) {
            if (val < min) {
                min = val;
            }
        }
        return min;
    }

    protected void initialValue() {
        super.initialValue();
        collect();
    }

    public static void collect() {
        TD.set(Unit.nanosToSeconds(getTickDurationMin()));
    }
}

