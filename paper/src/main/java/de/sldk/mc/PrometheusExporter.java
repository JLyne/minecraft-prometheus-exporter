package de.sldk.mc;

import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import de.sldk.mc.config.ExporterConfig;
import de.sldk.mc.core.ExporterPlugin;
import de.sldk.mc.core.server.MetricsServer;
import de.sldk.mc.metrics.Entities;
import de.sldk.mc.metrics.LoadedChunks;
import de.sldk.mc.metrics.PlayersOnlineTotal;
import de.sldk.mc.metrics.PlayersTotal;
import de.sldk.mc.metrics.TickDurationAverageCollector;
import de.sldk.mc.metrics.TickDurationMaxCollector;
import de.sldk.mc.metrics.TickDurationMedianCollector;
import de.sldk.mc.metrics.TickDurationMinCollector;
import de.sldk.mc.metrics.Tps;
import de.sldk.mc.metrics.Villagers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PrometheusExporter extends JavaPlugin implements ExporterPlugin, Listener {
	private static PrometheusExporter instance;
	private ExporterConfig config;
	private MetricsServer server;

	public static PrometheusExporter getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		config = new ExporterConfig(this);
		config.load();
		config.enableConfiguredMetrics();

		server = new MetricsServer(this);
		server.startServer();

		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		if (server != null) {
			server.stopServer();
		}

		config.destroyMetrics();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityAdd(EntityAddToWorldEvent event) {
		EntityType type = event.getEntityType();
		Entities.addEntity(type, event.getWorld());

      	if (event.getEntity() instanceof Villager villager) {
			Villagers.addVillager(villager.getWorld(), villager.getProfession());
        }
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityRemove(EntityRemoveFromWorldEvent event) {
		EntityType type = event.getEntityType();
		Entities.removeEntity(type, event.getWorld());

        if (event.getEntity() instanceof Villager villager) {
			Villagers.removeVillager(villager.getWorld(), villager.getProfession());
        }
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event) {
		LoadedChunks.addChunk(event.getWorld());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkUnload(ChunkUnloadEvent event) {
		LoadedChunks.removeChunk(event.getWorld());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		PlayersOnlineTotal.addPlayer(event.getPlayer().getWorld());

		if (!event.getPlayer().hasPlayedBefore()) {
			PlayersTotal.addPlayer();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		PlayersOnlineTotal.removePlayer(event.getPlayer().getWorld());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
			PlayersOnlineTotal.removePlayer(event.getFrom().getWorld());
			PlayersOnlineTotal.addPlayer(event.getTo().getWorld());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityTeleport(EntityTeleportEvent event) {
		if (event.getTo() == null) {
			return;
		}

		EntityType type = event.getEntityType();

		if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
			Entities.removeEntity(type, event.getFrom().getWorld());
			Entities.addEntity(type, event.getTo().getWorld());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onVillagerCareerChange(VillagerCareerChangeEvent event) {
		Villagers.removeVillager(event.getEntity().getWorld(), event.getEntity().getProfession());
		Villagers.addVillager(event.getEntity().getWorld(), event.getProfession());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onServerTickEnd(ServerTickEndEvent event) {
		TickDurationMaxCollector.collect();
		TickDurationMinCollector.collect();
		TickDurationAverageCollector.collect();
		TickDurationMedianCollector.collect();
		Tps.collect();
	}

	@Override
	public ExporterConfig getExporterConfig() {
		return config;
	}
}
