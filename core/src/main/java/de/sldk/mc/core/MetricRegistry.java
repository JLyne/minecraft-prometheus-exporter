package de.sldk.mc.core;

import java.util.ArrayList;
import java.util.List;
import de.sldk.mc.metrics.AbstractMetric;

public class MetricRegistry {
    private static final MetricRegistry INSTANCE = new MetricRegistry();
    
    private final List<AbstractMetric> metrics = new ArrayList<>();

    private MetricRegistry() {
        
    }
    
    public static MetricRegistry getInstance() {
        return INSTANCE;
    }
    
    public void register(AbstractMetric metric) {
        this.metrics.add(metric);
    }

    public void collectMetrics() {
        this.metrics.forEach(AbstractMetric::collect);
    }
}
