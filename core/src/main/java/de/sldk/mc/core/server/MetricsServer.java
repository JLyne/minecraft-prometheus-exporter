package de.sldk.mc.core.server;

import de.sldk.mc.core.ExporterPlugin;
import io.prometheus.metrics.exporter.httpserver.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;

public class MetricsServer {
    protected final ExporterPlugin exporter;
    private HTTPServer server;

    public MetricsServer(ExporterPlugin exporter) {
        this.exporter = exporter;
    }

    public void startServer() {
        int port = exporter.getExporterConfig().getPort();
        String host = exporter.getExporterConfig().getHost();

        if (server != null) {
            return;
        }

        InetSocketAddress address = new InetSocketAddress(host, port);
		try {
			server = HTTPServer.builder()
					.inetAddress(address.getAddress())
					.port(port)
					.buildAndStart();
            exporter.getLogger().info("Started Prometheus metrics endpoint at: " + host + ":" + port);
		} catch (IOException e) {
			throw new RuntimeException("Could not start Prometheus metrics endpoint", e);
		}
    }

    public void stopServer() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                exporter.getLogger().log(Level.WARNING, "Failed to stop Prometheus metrics endpoint gracefully", e);
            } finally {
                server = null;
            }
        }
    }
}
