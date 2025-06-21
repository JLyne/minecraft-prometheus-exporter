package de.sldk.mc.metrics;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;

public class TickDurationAverageCollector extends TickDurationCollector {
    private static final Gauge TD = Gauge.builder()
            .name(prefix("tick_duration_average"))
            .help("Average duration of server tick")
            .unit(Unit.SECONDS)
            .build();

    public TickDurationAverageCollector(PrometheusExporter plugin) {
        super(plugin, TD);
    }

    private static long getTickDurationAverage() {
        long sum = 0;
        long[] durations = getTickDurations();
        for (Long val : durations) {
            sum += val;
        }
        return sum / durations.length;
    }

    protected void initialValue() {
        super.initialValue();
        collect();
    }

    public static void collect() {
        TD.set(Unit.nanosToSeconds(getTickDurationAverage()));
    }
}
