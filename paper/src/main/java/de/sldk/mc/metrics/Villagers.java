package de.sldk.mc.metrics;

import io.prometheus.metrics.core.metrics.Gauge;
import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Get total count of Villagers.
 * <p>
 * Labelled by
 * <ul>
 *     <li> World ({@link World#getName()})
 *     <li> Type, e.g. 'desert', 'plains ({@link org.bukkit.entity.Villager.Type})
 *     <li> Profession, e.g. 'fisherman', 'farmer', or 'none' ({@link org.bukkit.entity.Villager.Profession})
 *     <li> Level ({@link Villager#getVillagerLevel()})
 * </ul>
 */
public class Villagers extends WorldMetric {
    private static final Gauge VILLAGERS = Gauge.builder()
            .name(prefix("villagers"))
            .help("Villagers total count, labelled by world and profession")
            .labelNames("world", "profession")
            .build();

    public Villagers(Plugin plugin) {
        super(plugin, VILLAGERS);
    }

    @Override
    protected void initialValue(World world) {
        Map<Villager.Profession, Long> mapVillagerGroupingToCount = world
                .getEntitiesByClass(Villager.class).stream()
                .collect(Collectors.groupingBy(Villager::getProfession, Collectors.counting()));

        mapVillagerGroupingToCount.forEach((profession, count) ->
                VILLAGERS
                        .labelValues(world.getName(), profession.getKey().getKey())
                        .set(count)
        );
    }

    public static void addVillager(World world, Villager.Profession profession) {
        VILLAGERS.labelValues(world.getName(), profession.getKey().getKey()).inc();
    }

    public static void removeVillager(World world, Villager.Profession profession) {
        VILLAGERS.labelValues(world.getName(), profession.getKey().getKey()).dec();
    }
}
