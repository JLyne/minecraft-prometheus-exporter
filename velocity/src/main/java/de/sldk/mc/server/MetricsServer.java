package de.sldk.mc.server;

import de.sldk.mc.PrometheusExporter;
import de.sldk.mc.core.server.AbstractServer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class MetricsServer extends AbstractServer {

	public MetricsServer(PrometheusExporter exporter) {
		super(exporter);
	}

	@Override
	protected Future<Object> collectMetrics() {
		metricRegistry.collectMetrics();
		return CompletableFuture.completedFuture(null);
	}
}
