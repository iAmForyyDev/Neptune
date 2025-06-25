package dev.lrxh.neptune.game.match.listener;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import dev.lrxh.neptune.API;
import dev.lrxh.neptune.Neptune;
import dev.lrxh.neptune.game.arena.impl.StandAloneArena;
import dev.lrxh.neptune.game.kit.impl.KitRule;
import dev.lrxh.neptune.game.match.Match;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.utils.EntityUtils;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class BlockTracker implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        getMatchForPlayer(event.getPlayer()).ifPresent(match ->
                match.getChanges().computeIfAbsent(event.getBlock().getLocation(), location -> event.getBlockReplacedState().getBlockData())
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player player) {
            this.getMatchForPlayer(player).ifPresent(match -> match.getEntities().add(event.getEntity()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCrystalPlace(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof EnderCrystal crystal)) {
            return;
        }

        if (event.getEntity().getEntitySpawnReason() != CreatureSpawnEvent.SpawnReason.DEFAULT) {
            return;
        }

        final Player player = getPlayer(crystal.getLocation());
        if (player == null) {
            event.setCancelled(true);
            return;
        }

        getMatchForPlayer(player).ifPresent(match -> match.getEntities().add(crystal));
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent event) {
        getMatchForPlayer(event.getPlayer()).ifPresent(match -> {
            if (match.getArena() instanceof StandAloneArena standAloneArena) {
                event.getItems().removeIf(item -> !standAloneArena.getWhitelistedBlocks().contains(item.getItemStack().getType()));
            } else {
                match.getEntities().addAll(event.getItems());
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EnderCrystal enderCrystal && event.getDamager() instanceof Player player) {
            enderCrystal.getPersistentDataContainer()
                    .set(new NamespacedKey(Neptune.get(), "neptune_crystal_owner"),
                            org.bukkit.persistence.PersistentDataType.STRING,
                            player.getUniqueId().toString());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExplosion(final @NotNull EntityExplodeEvent event) {
        if (event.getEntity() instanceof EnderCrystal enderCrystal) {
            final String uuid = enderCrystal.getPersistentDataContainer()
                    .get(new NamespacedKey(Neptune.get(), "neptune_crystal_owner"),
                            org.bukkit.persistence.PersistentDataType.STRING);

            if (uuid == null || uuid.isEmpty()) {
                return;
            }

            final Player player = Bukkit.getPlayer(UUID.fromString(uuid));
            if (player == null) {
                event.setCancelled(true);
                return;
            }

            getMatchForPlayer(player).ifPresent(match -> {
                for (final Block block : new ArrayList<>(event.blockList())) {
                    for (final ItemStack item : block.getDrops()) {
                        Bukkit.getScheduler().runTaskLater(Neptune.get(), () ->
                                match.getEntities().add(EntityUtils.getEntityByItemStack(player.getWorld(), item)), 5L
                        );
                    }

                    if (match.getKit().is(KitRule.ALLOW_ARENA_BREAK)) {
                        if (match.getArena() instanceof StandAloneArena arena) {
                            if (!arena.getWhitelistedBlocks().contains(block.getType())) {
                                match.getChanges().computeIfAbsent(block.getLocation(), location -> block.getBlockData());
                            } else {
                                if (match.getChanges().containsKey(block.getLocation())) {
                                    event.blockList().remove(block);
                                } else {
                                    match.getChanges().put(block.getLocation(), block.getBlockData());
                                }
                            }
                        }
                    } else {
                        if (match.getChanges().containsKey(block.getLocation())) {
                            event.blockList().remove(block);
                        } else {
                            match.getChanges().put(block.getLocation(), block.getBlockData());
                        }
                    }
                }
            });
        } else {
            final Player player = getPlayer(event.getLocation());
            if (player == null) {
                event.setCancelled(true);
                return;
            }

            getMatchForPlayer(player).ifPresent(match -> {
                for (final Block block : new ArrayList<>(event.blockList())) {
                    for (final ItemStack item : block.getDrops()) {
                        Bukkit.getScheduler().runTaskLater(Neptune.get(), () ->
                                match.getEntities().add(EntityUtils.getEntityByItemStack(player.getWorld(), item)), 5L
                        );
                    }

                    if (match.getKit().is(KitRule.ALLOW_ARENA_BREAK)) {
                        if (match.getArena() instanceof StandAloneArena arena) {
                            if (!arena.getWhitelistedBlocks().contains(block.getType())) {
                                match.getChanges().computeIfAbsent(block.getLocation(), location -> block.getBlockData());
                            } else {
                                if (match.getChanges().containsKey(block.getLocation())) {
                                    event.blockList().remove(block);
                                } else {
                                    match.getChanges().put(block.getLocation(), block.getBlockData());
                                }
                            }
                        }
                    } else {
                        if (match.getChanges().containsKey(block.getLocation())) {
                            event.blockList().remove(block);
                        } else {
                            match.getChanges().put(block.getLocation(), block.getBlockData());
                        }
                    }
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        getMatchForPlayer(event.getPlayer()).ifPresent(match -> {
                    match.getLiquids().add(
                            event.getBlockClicked().getRelative(event.getBlockFace()).getLocation()
                    );
                }
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        final Block toBlock = event.getToBlock();
        final Player player = getPlayer(toBlock.getLocation());

        if (player == null) {
            event.setCancelled(true);
            return;
        }

        getMatchForPlayer(player).ifPresent(match ->
                match.getChanges().computeIfAbsent(toBlock.getLocation(), location -> Material.AIR.createBlockData())
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakBlockEvent event) {
        event.getDrops().clear();
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onDestroy(BlockDestroyEvent event) {
        final Block block = event.getBlock();
        block.getDrops().clear();
        event.setWillDrop(false);
        final Player player = getPlayer(block.getLocation());
        if (player == null) {
            event.setCancelled(true);
            return;
        }

        getMatchForPlayer(player).ifPresent(match ->
                match.getChanges().computeIfAbsent(block.getLocation(), location -> block.getBlockData())
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        getMatchForPlayer(event.getPlayer()).ifPresent(match ->
                match.getChanges().computeIfAbsent(event.getBlock().getLocation(), location -> event.getBlock().getBlockData())
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMultiPlace(BlockMultiPlaceEvent event) {
        getMatchForPlayer(event.getPlayer()).ifPresent(match -> {
            for (final BlockState blockState : event.getReplacedBlockStates()) {
                match.getChanges().computeIfAbsent(blockState.getLocation(), location -> blockState.getBlockData());
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent event) {
        final Block block = event.getBlock();
        final Player player = getPlayer(block.getLocation());

        if (player != null) {
            getMatchForPlayer(player).ifPresent(match ->
                    match.getChanges().computeIfAbsent(block.getLocation(), loc -> block.getBlockData())
            );
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        final Player player = getPlayer(event.getBlock().getLocation());
        if (player == null) {
            return;
        }

        getMatchForPlayer(player).ifPresent(match ->
                match.getChanges().computeIfAbsent(event.getBlock().getLocation(), loc -> event.getNewState().getBlockData())
        );
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExplode(BlockExplodeEvent event) {
        final Player player = getPlayer(event.getBlock().getLocation());
        if (player == null) {
            event.setCancelled(true);
            return;
        }

        getMatchForPlayer(player).ifPresent(match -> {
            for (final Block block : event.blockList()) {
                match.getChanges().computeIfAbsent(block.getLocation(), location -> block.getBlockData());
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        event.setFire(false);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        switch (event.getCause()) {
            case LIGHTNING, FIREBALL, EXPLOSION -> event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onHangingBreak(HangingBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    private Player getPlayer(Location location) {
        Player player = null;
        for (final Entity entity : location.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof Player p) {
                player = p;
                break;
            }
        }
        return player;
    }

    private Optional<Match> getMatchForPlayer(Player player) {
        final Profile profile = API.getProfile(player);
        return Optional.ofNullable(profile)
                .map(Profile::getMatch);
    }
}