package de.sldk.mc.metrics;

import java.util.Arrays;

import de.sldk.mc.PrometheusExporter;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;

public class TickDurationMedianCollector extends TickDurationCollector {
    private static final Gauge TD = Gauge.builder()
            .name(prefix("tick_duration_median"))
            .help("Median duration of server tick (nanoseconds)")
            .unit(Unit.SECONDS)
            .build();

    public TickDurationMedianCollector(PrometheusExporter plugin) {
        super(plugin, TD);
    }

    private static long getTickDurationMedian() {
        /* Copy the original array - don't want to sort it! */
        long[] tickTimes = getTickDurations().clone();
        Arrays.sort(tickTimes);
        return tickTimes[tickTimes.length / 2];
    }

    protected void initialValue() {
        super.initialValue();
        collect();
    }

    public static void collect() {
        TD.set(Unit.nanosToSeconds(getTickDurationMedian()));
    }
}
