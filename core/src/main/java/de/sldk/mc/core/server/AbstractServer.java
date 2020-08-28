package de.sldk.mc.core.server;

import de.sldk.mc.core.MetricRegistry;
import de.sldk.mc.core.ExporterPlugin;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;

public abstract class AbstractServer extends AbstractHandler {
    protected final MetricRegistry metricRegistry = MetricRegistry.getInstance();
    protected final ExporterPlugin exporter;
    private Server server;

    public AbstractServer(ExporterPlugin exporter) {
        this.exporter = exporter;
    }

    public void startServer() {
        int port = exporter.getExporterConfig().getPort();
        String host = exporter.getExporterConfig().getHost();

        InetSocketAddress address = new InetSocketAddress(host, port);
        server = new Server(address);
        server.setHandler(this);

        try {
            server.start();
            exporter.getLogger().info("Started Prometheus metrics endpoint at: " + host + ":" + port);

        } catch (Exception e) {
            exporter.getLogger().severe("Could not start embedded Jetty server");
        }
    }

    public void stopServer() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                exporter.getLogger().log(Level.WARNING, "Failed to stop metrics server gracefully: " + e.getMessage());
                exporter.getLogger().log(Level.FINE, "Failed to stop metrics server gracefully", e);
            }
        }
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!target.equals("/metrics")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            collectMetrics().get();

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(TextFormat.CONTENT_TYPE_004);

            TextFormat.write004(response.getWriter(), CollectorRegistry.defaultRegistry.metricFamilySamples());

            baseRequest.setHandled(true);
        } catch (InterruptedException | ExecutionException e) {
            exporter.getLogger().log(Level.WARNING, "Failed to read server statistic: " + e.getMessage());
            exporter.getLogger().log(Level.FINE, "Failed to read server statistic: ", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected abstract Future<Object> collectMetrics();
}
