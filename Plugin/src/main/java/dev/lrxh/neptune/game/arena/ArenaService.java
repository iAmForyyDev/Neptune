package dev.lrxh.neptune.game.arena;

import dev.lrxh.neptune.configs.ConfigService;
import dev.lrxh.neptune.game.arena.impl.ArenaType;
import dev.lrxh.neptune.game.arena.impl.SharedArena;
import dev.lrxh.neptune.game.arena.impl.StandAloneArena;
import dev.lrxh.neptune.providers.manager.IService;
import dev.lrxh.neptune.providers.manager.Value;
import dev.lrxh.neptune.utils.ConfigFile;
import dev.lrxh.neptune.utils.LocationUtil;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

@Getter
public class ArenaService extends IService {
    private static ArenaService instance;
    public final LinkedHashSet<Arena> arenas = new LinkedHashSet<>();

    public static ArenaService get() {
        if (instance == null) instance = new ArenaService();

        return instance;
    }

    @Override
    public void load() {
        FileConfiguration config = ConfigService.get().getArenasConfig().getConfiguration();
        if (config.contains("arenas")) {
            for (String arenaName : getKeys("arenas")) {
                Arena arena = loadArena(arenaName);
                arenas.add(arena);
            }
        }
    }

    public Arena loadArena(String arenaName) {
        FileConfiguration config = ConfigService.get().getArenasConfig().getConfiguration();
        String path = "arenas." + arenaName + ".";

        if (!config.contains(path + "displayName")) return null;

        String displayName = config.getString(path + "displayName");
        Location redSpawn = LocationUtil.deserialize(config.getString(path + "redSpawn"));
        Location blueSpawn = LocationUtil.deserialize(config.getString(path + "blueSpawn"));
        boolean enabled = config.getBoolean(path + "enabled");
        ArenaType arenaType = ArenaType.valueOf(config.getString(path + "type"));
        int deathY = config.getInt(path + "deathY", -68321);

        Arena arena;

        if (arenaType.equals(ArenaType.STANDALONE)) {
            Location edge1 = LocationUtil.deserialize(config.getString(path + "min"));
            Location edge2 = LocationUtil.deserialize(config.getString(path + "max"));

            double limit = config.getDouble(path + "limit");
            List<String> copies = config.getStringList(path + "copies");
            List<StandAloneArena> copiesArenas = new ArrayList<>();

            for (String copyName : new ArrayList<>(copies)) {
                Arena copy = loadArena(copyName);
                if (copy == null) copies.remove(copiesArenas);
                copiesArenas.add((StandAloneArena) copy);
                arenas.add(copy);
            }

            boolean copy = config.getBoolean(path + "copy", false);
            List<Material> whitelistedBlocks = new ArrayList<>();

            for (String name : config.getStringList(path + "whitelistedBlocks")) {
                whitelistedBlocks.add(Material.getMaterial(name));
            }

            arena = new StandAloneArena(arenaName, displayName, redSpawn, blueSpawn, edge1, edge2, limit, enabled, copy, copiesArenas, whitelistedBlocks, deathY);
        } else {
            arena = new SharedArena(arenaName, displayName, redSpawn, blueSpawn, enabled, deathY);
        }

        return arena;
    }


    @Override
    public void save() {
        getConfigFile().getConfiguration().getKeys(false).forEach(key -> getConfigFile().getConfiguration().set(key, null));
        arenas.forEach(arena -> {
            String path = "arenas." + arena.getName() + ".";
            List<Value> values = new ArrayList<>(Arrays.asList(
                    new Value("displayName", arena.getDisplayName()),
                    new Value("redSpawn", LocationUtil.serialize(arena.getRedSpawn())),
                    new Value("blueSpawn", LocationUtil.serialize(arena.getBlueSpawn())),
                    new Value("enabled", arena.isEnabled()),
                    new Value("deathY", arena.getDeathY())
            ));
            if (arena instanceof StandAloneArena standAloneArena) {
                values.addAll(Arrays.asList(
                        new Value("type", "STANDALONE"),
                        new Value("min", LocationUtil.serialize(standAloneArena.getMin())),
                        new Value("max", LocationUtil.serialize(standAloneArena.getMax())),
                        new Value("limit", standAloneArena.getLimit()),
                        new Value("copies", standAloneArena.getCopiesAsString()),
                        new Value("copy", standAloneArena.isCopy()),
                        new Value("whitelistedBlocks", standAloneArena.getWhitelistedBlocksAsString())
                ));
            } else {
                values.add(new Value("type", "SHARED"));
            }
            save(values, path);
        });
    }


    public Arena getArenaByName(String arenaName) {
        for (Arena arena : arenas) {
            if (arena != null && arena.getName() != null && arena.getName().equalsIgnoreCase(arenaName)) {
                return arena;
            }
        }
        return null;
    }


    @Override
    public ConfigFile getConfigFile() {
        return ConfigService.get().getArenasConfig();
    }
}
