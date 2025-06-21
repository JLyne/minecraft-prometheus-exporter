package de.sldk.mc;

import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.function.BiConsumer;

public class Util {
	public static <T> void collectForServers(T callback, BiConsumer<T, RegisteredServer> collector) {
		for (RegisteredServer world: PrometheusExporter.getInstance().getProxy().getAllServers()) {
			collector.accept(callback, world);
		}
	}
}
