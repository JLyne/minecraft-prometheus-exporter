package de.sldk.mc.core;

import de.sldk.mc.core.config.ExporterConfig;

import java.util.logging.Logger;

public interface ExporterPlugin {
    Logger getLogger();

    ExporterConfig<?> getExporterConfig();
}
