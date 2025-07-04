package dev.lrxh.neptune.game.match.listener;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.configs.impl.MessagesLocale;
import dev.lrxh.neptune.game.arena.Arena;
import dev.lrxh.neptune.game.arena.impl.StandAloneArena;
import dev.lrxh.neptune.game.kit.Kit;
import dev.lrxh.neptune.game.kit.impl.KitRule;
import dev.lrxh.neptune.game.match.Match;
import dev.lrxh.neptune.game.match.impl.MatchState;
import dev.lrxh.neptune.game.match.impl.participant.DeathCause;
import dev.lrxh.neptune.game.match.impl.participant.Participant;
import dev.lrxh.neptune.game.match.impl.participant.ParticipantColor;
import dev.lrxh.neptune.game.match.impl.team.TeamFightMatch;
import dev.lrxh.neptune.profile.ProfileService;
import dev.lrxh.neptune.profile.data.ProfileState;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.providers.clickable.Replacement;
import dev.lrxh.neptune.utils.CC;
import dev.lrxh.neptune.utils.LocationUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

public final class MatchListener implements Listener {

    @EventHandler
    public void onBlockPlaceEvent(final @NotNull BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        final Profile profile = API.getProfile(player);
        if (profile == null) {
            return;
        }

        final Match match = profile.getMatch();
        final Location blockLocation = event.getBlock().getLocation();

        if (profile.hasState(ProfileState.IN_KIT_EDITOR)) {
            event.setCancelled(true);
            player.sendMessage(CC.color("&cYou can't place blocks here!"));
            return;
        }

        if (match != null && match.getKit().is(KitRule.BUILD)) {
            if (match.getState().equals(MatchState.STARTING)) {
                event.setCancelled(true);
                player.sendMessage(CC.color("&cYou can't place blocks yet!"));
                return;
            }

            final StandAloneArena arena = (StandAloneArena) match.arena;

            // Check height limit
            if (blockLocation.getY() >= arena.getLimit()) {
                event.setCancelled(true);
                player.sendMessage(CC.color("&cYou have reached build limit!"));
                return;
            }

            // Check arena boundaries
            if (!LocationUtil.isInside(blockLocation, arena.getMin(), arena.getMax())) {
                event.setCancelled(true);
                player.sendMessage(CC.color("&cYou can't build outside the arena!"));
                return;
            }

            match.getPlacedBlocks().add(blockLocation);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(final @NotNull PlayerDeathEvent event) {
        final Player player = event.getEntity();
        event.deathMessage(null);
        event.getDrops().clear();
        final Profile profile = API.getProfile(player);
        if (profile == null) {
            return;
        }

        if (profile.getMatch() != null) {
            final Match match = profile.getMatch();
            final Participant participant = match.getParticipant(player.getUniqueId());
            if (participant == null) {
                return;
            }

            participant.setDeathCause(participant.getLastAttacker() != null ? DeathCause.KILL : DeathCause.DIED);
            match.onDeath(participant);
        }
    }

    @EventHandler
    public void onPlayerDamage(final @NotNull EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker && event.getEntity() instanceof Player) {
            final Profile profile = API.getProfile(attacker);
            if (profile == null) {
                return;
            }

            final Match match = profile.getMatch();
            if (match == null) {
                return;
            }

            if (match.getKit().is(KitRule.PARKOUR)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPressurePlate(final @NotNull PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction() == Action.PHYSICAL) {
            final Material blockType = event.getClickedBlock() != null ? event.getClickedBlock().getType() : null;
            if (blockType == null) {
                return;
            }

            final Profile profile = API.getProfile(player);
            if (profile == null) {
                return;
            }

            final Match match = profile.getMatch();
            if (match == null) {
                return;
            }

            if (!match.getKit().is(KitRule.PARKOUR)) {
                return;
            }

            if (!match.getState().equals(MatchState.IN_ROUND)) {
                return;
            }

            final Participant participant = match.getParticipant(player);
            if (participant == null) {
                return;
            }

            if (blockType.equals(Material.HEAVY_WEIGHTED_PRESSURE_PLATE)) {
                if (participant.setCurrentCheckPoint(event.getClickedBlock().getLocation().clone().add(0, 1, 0))) {
                    match.broadcast(MessagesLocale.PARKOUR_CHECKPOINT,
                            new Replacement("<player>", participant.getNameColored()),
                            new Replacement("<checkpoint>", String.valueOf(participant.getCheckPoint())),
                            new Replacement("<time>", participant.getTime().formatDuration()));
                }
            } else if (blockType.equals(Material.LIGHT_WEIGHTED_PRESSURE_PLATE)) {
                match.win(participant);
                match.broadcast(MessagesLocale.PARKOUR_END,
                        new Replacement("<player>", participant.getNameColored()),
                        new Replacement("<time>", participant.getTime().formatDuration()));
            }
        }
    }

    @EventHandler
    public void onItemPickup(final @NotNull EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                return;
            }

            final Profile profile = API.getProfile(player);
            if (!profile.getState().equals(ProfileState.IN_GAME)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(final @NotNull PlayerBucketEmptyEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        final Profile profile = API.getProfile(player);
        if (profile == null) {
            return;
        }

        final Match match = profile.getMatch();
        final Location blockLocation = event.getBlock().getLocation();
        if (profile.hasState(ProfileState.IN_KIT_EDITOR)) {
            event.setCancelled(true);
            player.sendMessage(CC.color("&cYou can't place blocks here!"));
            return;
        }

        if (match != null && match.getKit().is(KitRule.BUILD)) {
            if (match.getState().equals(MatchState.STARTING)) {
                event.setCancelled(true);
                player.sendMessage(CC.color("&cYou can't place blocks yet!"));
                return;
            }

            final StandAloneArena arena = (StandAloneArena) match.arena;
            if (blockLocation.getY() >= arena.getLimit()) {
                event.setCancelled(true);
                player.sendMessage(CC.color("&cYou have reached build limit!"));
                return;
            }

            // Check arena boundaries for bucket empty
            if (!LocationUtil.isInside(blockLocation, arena.getMin(), arena.getMax())) {
                event.setCancelled(true);
                player.sendMessage(CC.color("&cYou can't place water outside the arena!"));
                return;
            }

            match.getPlacedBlocks().add(blockLocation);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileLaunch(final @NotNull ProjectileLaunchEvent event) {
        final ProjectileSource shooter = event.getEntity().getShooter();
        if (!(shooter instanceof Player player)) {
            return;
        }

        final Profile profile = API.getProfile(player);
        final Match match = profile.getMatch();
        if (match == null) {
            event.setCancelled(true);
            return;
        }

        if (match.getState().equals(MatchState.STARTING)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntityMonitor(final @NotNull EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player player) {
            // Experimental feature
            final DamageSource damageSource = event.getDamageSource();
            if (damageSource.getCausingEntity() instanceof Player attacker) {
                final Profile attackerProfile = API.getProfile(attacker.getUniqueId());
                final Profile profile = API.getProfile(player);
                if (profile == null) {
                    return;
                }

                if (profile.getMatch() == null || attackerProfile.getState().equals(ProfileState.IN_SPECTATOR)) {
                    event.setCancelled(true);
                    return;
                }

                final Match match = profile.getMatch();
                if (!attackerProfile.getMatch().getUuid().equals(match.getUuid())) {
                    event.setCancelled(true);
                    return;
                }

                if (match.getParticipant(attacker).isDead()) {
                    event.setCancelled(true);
                }

                if (match instanceof TeamFightMatch teamFightMatch) {
                    if (player == attacker) {
                        return;
                    }

                    if (teamFightMatch.onSameTeam(player.getUniqueId(), attacker.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }

                if (!match.state.equals(MatchState.IN_ROUND)) {
                    event.setCancelled(true);
                } else {
                    if (!match.getKit().is(KitRule.DAMAGE)) {
                        event.setDamage(0);
                    }
                }

                match.getParticipant(player.getUniqueId()).setLastAttacker(match.getParticipant(attacker.getUniqueId()));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerHitEvent(final @NotNull EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player target && event.getDamager() instanceof Player damager) {
            final Profile targetProfile = API.getProfile(target);
            final Profile playerProfile = API.getProfile(damager.getUniqueId());
            if (targetProfile.getState() == ProfileState.IN_GAME && playerProfile.getState().equals(ProfileState.IN_GAME) && damager.getAttackCooldown() >= 0.2) {
                final Match match = targetProfile.getMatch();
                final Participant opponent = match.getParticipant(target.getUniqueId());
                match.getParticipant(damager.getUniqueId()).handleHit(opponent);
                opponent.resetCombo();
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(final @NotNull EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!(event.getFinalDamage() >= player.getHealth())) {
            return;
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.TOTEM_OF_UNDYING) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.TOTEM_OF_UNDYING)) {
            return;
        }

        final Profile profile = API.getProfile(player);
        if (profile == null) {
            return;
        }

        final Match match = profile.getMatch();
        if (match == null) {
            return;
        }

        final Participant participant = match.getParticipant(player.getUniqueId());
        participant.setDeathCause(DeathCause.DIED);
        match.onDeath(participant);
        player.setHealth(20.0f);
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMoveEvent(final @NotNull PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) {
            return;
        }

        final Player player = event.getPlayer();
        final Profile profile = API.getProfile(player);
        if (profile == null) {
            return;
        }

        final Match match = profile.getMatch();
        if (match != null) {
            final Participant participant = match.getParticipant(player.getUniqueId());
            if (participant == null) {
                return;
            }

            if (participant.isFrozen()) {
                event.setCancelled(true);
                return;
            }

            final Arena arena = match.getArena();
            if (player.getLocation().getY() <= arena.getDeathY() && !participant.isDead()) {
                if (match.getKit().is(KitRule.PARKOUR)) {
                    if (participant.getCurrentCheckPoint() != null) {
                        player.teleport(participant.getCurrentCheckPoint());
                    } else {
                        player.teleport(match.getSpawn(participant));
                    }
                } else {
                    participant.setDeathCause(DeathCause.DIED);
                    match.onDeath(participant);
                }
                return;
            }
            if (match.getState().equals(MatchState.IN_ROUND)) {
                final Location playerLocation = player.getLocation();
                if (match.getKit().is(KitRule.DROPPER)) {
                    final Block block = playerLocation.getBlock();
                    if (block.getType() == Material.WATER) {
                        match.win(participant);
                    }

                    return;
                }

                if (match.getKit().is(KitRule.SUMO)) {
                    final Block block = playerLocation.getBlock();
                    if (block.getType() == Material.WATER) {
                        participant.setDeathCause(participant.getLastAttacker() != null ? DeathCause.KILL : DeathCause.DIED);
                        match.onDeath(participant);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        final Material blockType = clickedBlock.getType();
        if (this.isStrippable(blockType)) {
            event.setCancelled(true);
        }
    }

    private boolean isStrippable(final @NotNull Material material) {
        return material == Material.OAK_LOG ||
                material == Material.SPRUCE_LOG ||
                material == Material.BIRCH_LOG ||
                material == Material.JUNGLE_LOG ||
                material == Material.ACACIA_LOG ||
                material == Material.DARK_OAK_LOG ||
                material == Material.MANGROVE_LOG ||
                material == Material.CHERRY_LOG ||
                material == Material.CRIMSON_STEM ||
                material == Material.COPPER_BLOCK ||
                material == Material.WARPED_STEM;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(final @NotNull EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            final Profile profile = API.getProfile(player);
            if (profile == null) {
                return;
            }

            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                return;
            }

            final Match match = profile.getMatch();
            if (match == null) {
                event.setCancelled(true);
                return;
            }

            if (profile.hasState(ProfileState.IN_SPECTATOR)) {
                event.setCancelled(true);
            }

            final Kit kit = match.getKit();
            if (match.getState().equals(MatchState.STARTING) || match.getState().equals(MatchState.ENDING)) {
                event.setCancelled(true);
                return;
            }

            if (!kit.is(KitRule.FALL_DAMAGE) && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                event.setCancelled(true);
                return;
            }

            if (!kit.is(KitRule.DAMAGE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(final @NotNull FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            final Profile profile = API.getProfile(player);
            if (profile == null) {
                return;
            }

            final Match match = profile.getMatch();
            if (!profile.getState().equals(ProfileState.IN_GAME)) {
                event.setCancelled(true);
                return;
            }

            if (match == null) {
                return;
            }

            if (!match.getKit().is(KitRule.HUNGER)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(final @NotNull EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {
            final Profile profile = API.getProfile(player);
            if (profile == null) {
                return;
            }

            final Match match = profile.getMatch();
            if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
                if (match != null && !match.getKit().is(KitRule.SATURATION_HEAL)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(final @NotNull BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        final Profile profile = API.getProfile(player);
        if (profile == null) {
            return;
        }

        if (profile.getState().equals(ProfileState.IN_LOBBY)) {
            event.setCancelled(true);
            return;
        }

        if (profile.getState().equals(ProfileState.IN_SPECTATOR)) {
            event.setCancelled(true);
            return;
        }

        final Match match = profile.getMatch();
        final Location blockLocation = event.getBlock().getLocation();
        final Material blockType = event.getBlock().getType();
        if (match == null) {
            return;
        }

        if (blockType.name().contains("BED")) {
            return;
        }

        if (match.getKit().is(KitRule.BUILD)) {
            event.setCancelled(!match.getPlacedBlocks().contains(blockLocation));
        } else {
            event.setCancelled(true);
        }

        if (match.getKit().is(KitRule.BUILD)) {
            event.setCancelled(!match.getPlacedBlocks().contains(blockLocation));
        } else {
            event.setCancelled(true);
        }

        if (match.getKit().is(KitRule.ALLOW_ARENA_BREAK)) {
            if (match.getArena() instanceof StandAloneArena standAloneArena) {
                event.setCancelled(!standAloneArena.getWhitelistedBlocks().contains(blockType));
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler()
    public void onBedBreak(final @NotNull BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Profile profile = ProfileService.get().getByUUID(player.getUniqueId());
        final Match match = profile.getMatch();
        final Material blockType = event.getBlock().getType();
        if (match == null) {
            return;
        }

        if (!match.getKit().is(KitRule.BED_WARS)) {
            return;
        }

        if (match.getKit().is(KitRule.BED_WARS)) {
            if (blockType == Material.OAK_PLANKS || blockType == Material.END_STONE) {
                event.setCancelled(false);
            }
        }

        if (match.getKit().is(KitRule.BED_WARS)) {
            if (event.getBlock().getType().toString().contains("BED")) {
                final Location bed = event.getBlock().getLocation();
                final Participant participant = match.getParticipant(player.getUniqueId());
                if (participant == null) {
                    return;
                }

                final Location spawn = match.getSpawn(participant);
                final Participant opponent = participant.getOpponent();
                final Location opponentSpawn = match.getSpawn(opponent);
                final ParticipantColor color = participant.getColor();

                if (bed.distanceSquared(spawn) > bed.distanceSquared(opponentSpawn)) {
                    event.setDropItems(false);
                    match.breakBed(opponent);
                    match.sendTitle(opponent, CC.color(MessagesLocale.BED_BREAK_TITLE.getString()), CC.color(MessagesLocale.BED_BREAK_FOOTER.getString()), 20);
                    match.broadcast(color.equals(ParticipantColor.RED) ? MessagesLocale.BLUE_BED_BROKEN_MESSAGE : MessagesLocale.RED_BED_BROKEN_MESSAGE, new Replacement("<player>", participant.getNameColored()));
                } else {
                    event.setCancelled(true);
                    participant.sendMessage(MessagesLocale.CANT_BREAK_OWN_BED);
                }

            }
        }
    }

    @EventHandler
    public void onProjectileHit(final @NotNull ProjectileHitEvent event) {
        final Projectile projectile = event.getEntity();
        final ProjectileSource shooter = projectile.getShooter();

        if (shooter instanceof Player player) {
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                return;
            }

            final Profile profile = API.getProfile(player);
            if (profile == null) {
                return;
            }

            if (!profile.getState().equals(ProfileState.IN_GAME)) {
                event.setCancelled(true);
            } else {
                getMatchForPlayer(player).ifPresent(match ->
                        match.getEntities().add(projectile)
                );
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplode(final @NotNull BlockExplodeEvent event) {
        event.setYield(0);
        final Player player = getPlayer(event.getBlock().getLocation());
        if (player == null) {
            event.setCancelled(true);
            return;
        }

        getMatchForPlayer(player).ifPresent(match -> {
            for (final Block block : new ArrayList<>(event.blockList())) {
                if (match.getKit().is(KitRule.ALLOW_ARENA_BREAK)) {
                    if (match.getArena() instanceof StandAloneArena standAloneArena) {
                        if (!standAloneArena.getWhitelistedBlocks().contains(block.getType())) {
                            event.blockList().remove(block);
                        }
                    }
                }
            }
        });
    }

    @EventHandler
    public void onItemDrop(final @NotNull PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        final Profile profile = API.getProfile(player);
        if (profile == null) {
            return;
        }

        if (!profile.getState().equals(ProfileState.IN_GAME)) {
            event.setCancelled(true);
        } else {
            profile.getMatch().getEntities().add(event.getItemDrop());
        }
    }

    private Player getPlayer(final @NotNull Location location) {
        Player player = null;
        for (Entity entity : location.getNearbyEntities(10, 10, 10)) {
            if (entity instanceof Player p) player = p;
        }

        return player;
    }

    private Optional<Match> getMatchForPlayer(final @NotNull Player player) {
        final Profile profile = API.getProfile(player);
        return Optional.ofNullable(profile)
                .map(Profile::getMatch);
    }
}
