package dev.lrxh.neptune.configs.impl;

import dev.lrxh.neptune.configs.ConfigService;
import dev.lrxh.neptune.configs.impl.handler.DataType;
import dev.lrxh.neptune.configs.impl.handler.IDataAccessor;
import dev.lrxh.neptune.utils.ConfigFile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum HotbarLocale implements IDataAccessor {
    LOBBY_UNRANKED_NAME("ITEMS.IN_LOBBY.UNRANKED.NAME", DataType.STRING, "&aQueue Match &7(Right Click)"),
    LOBBY_UNRANKED_MATERIAL("ITEMS.IN_LOBBY.UNRANKED.MATERIAL", DataType.STRING, "IRON_SWORD"),
    LOBBY_UNRANKED_LORE("ITEMS.IN_LOBBY.UNRANKED.LORE", DataType.STRING_LIST, " "),
    LOBBY_UNRANKED_SLOT("ITEMS.IN_LOBBY.UNRANKED.SLOT", DataType.INT, "0"),
    LOBBY_UNRANKED_ENABLED("ITEMS.IN_LOBBY.UNRANKED.ENABLED", DataType.BOOLEAN, "true"),
    LOBBY_SPECTATE_MENU_NAME("ITEMS.IN_LOBBY.SPECTATE_MENU.NAME", DataType.STRING, "&6View Matches &7(Right Click)"),
    LOBBY_SPECTATE_MENU_MATERIAL("ITEMS.IN_LOBBY.SPECTATE_MENU.MATERIAL", DataType.STRING, "EMERALD"),
    LOBBY_SPECTATE_MENU_LORE("ITEMS.IN_LOBBY.SPECTATE_MENU.LORE", DataType.STRING_LIST, " "),
    LOBBY_SPECTATE_MENU_SLOT("ITEMS.IN_LOBBY.SPECTATE_MENU.SLOT", DataType.INT, "1"),
    LOBBY_SPECTATE_MENU_ENABLED("ITEMS.IN_LOBBY.SPECTATE_MENU.ENABLED", DataType.BOOLEAN, "false"),
    LOBBY_LEADERBOARDS_NAME("ITEMS.IN_LOBBY.LEADERBOARDS.NAME", DataType.STRING, "&cLeaderboards &7(Right Click)"),
    LOBBY_LEADERBOARDS_MATERIAL("ITEMS.IN_LOBBY.LEADERBOARDS.MATERIAL", DataType.STRING, "NETHER_STAR"),
    LOBBY_LEADERBOARDS_LORE("ITEMS.IN_LOBBY.LEADERBOARDS.LORE", DataType.STRING_LIST, " "),
    LOBBY_LEADERBOARDS_SLOT("ITEMS.IN_LOBBY.LEADERBOARDS.SLOT", DataType.INT, "1"),
    LOBBY_LEADERBOARDS_ENABLED("ITEMS.IN_LOBBY.LEADERBOARDS.ENABLED", DataType.BOOLEAN, "true"),
    LOBBY_KIT_EDITOR_NAME("ITEMS.IN_LOBBY.KIT_EDITOR.NAME", DataType.STRING, "&3Kit Editor &7(Right Click)"),
    LOBBY_KIT_EDITOR_MATERIAL("ITEMS.IN_LOBBY.KIT_EDITOR.MATERIAL", DataType.STRING, "BOOK"),
    LOBBY_KIT_EDITOR_LORE("ITEMS.IN_LOBBY.KIT_EDITOR.LORE", DataType.STRING_LIST, " "),
    LOBBY_KIT_EDITOR_SLOT("ITEMS.IN_LOBBY.KIT_EDITOR.SLOT", DataType.INT, "2"),
    LOBBY_KIT_EDITOR_ENABLED("ITEMS.IN_LOBBY.KIT_EDITOR.ENABLED", DataType.BOOLEAN, "true"),
    IN_GAME_PLAY_AGAIN_NAME("ITEMS.IN_GAME.PLAY_AGAIN.NAME", DataType.STRING, "&aPlay again &7(Right Click)"),
    IN_GAME_PLAY_AGAIN_MATERIAL("ITEMS.IN_GAME.PLAY_AGAIN.MATERIAL", DataType.STRING, "PAPER"),
    IN_GAME_PLAY_AGAIN_LORE("ITEMS.IN_GAME.PLAY_AGAIN.LORE", DataType.STRING_LIST, " "),
    IN_GAME_PLAY_AGAIN_SLOT("ITEMS.IN_GAME.PLAY_AGAIN.SLOT", DataType.INT, "3"),
    IN_GAME_PLAY_AGAIN_ENABLED("ITEMS.IN_GAME.PLAY_AGAIN.ENABLED", DataType.BOOLEAN, "true"),
    IN_GAME_REMATCH_NAME("ITEMS.IN_GAME.REMATCH.NAME", DataType.STRING, "&eRematch &7(Right Click)"),
    IN_GAME_REMATCH_MATERIAL("ITEMS.IN_GAME.REMATCH.MATERIAL", DataType.STRING, "LEAD"),
    IN_GAME_REMATCH_SLOT("ITEMS.IN_GAME.REMATCH.SLOT", DataType.INT, "4"),
    IN_GAME_REMATCH_ENABLED("ITEMS.IN_GAME.REMATCH.ENABLED", DataType.BOOLEAN, "true"),
    LOBBY_PARTY_CREATE_NAME("ITEMS.IN_LOBBY.PARTY_CREATE.NAME", DataType.STRING, "&9Create Party &7(Right Click)"),
    LOBBY_PARTY_CREATE_MATERIAL("ITEMS.IN_LOBBY.PARTY_CREATE.MATERIAL", DataType.STRING, "NAME_TAG"),
    LOBBY_PARTY_CREATE_LORE("ITEMS.IN_LOBBY.PARTY_CREATE.LORE", DataType.STRING_LIST, " "),
    LOBBY_PARTY_CREATE_SLOT("ITEMS.IN_LOBBY.PARTY_CREATE.SLOT", DataType.INT, "4"),
    LOBBY_PARTY_CREATE_ENABLED("ITEMS.IN_LOBBY.PARTY_CREATE.ENABLED", DataType.BOOLEAN, "true"),
    LOBBY_SETTINGS_NAME("ITEMS.IN_LOBBY.SETTINGS.NAME", DataType.STRING, "&dSettings &7(Right Click)"),
    LOBBY_SETTINGS_MATERIAL("ITEMS.IN_LOBBY.SETTINGS.MATERIAL", DataType.STRING, "COMPASS"),
    LOBBY_SETTINGS_LORE("ITEMS.IN_LOBBY.SETTINGS.LORE", DataType.STRING_LIST, " "),
    LOBBY_SETTINGS_SLOT("ITEMS.IN_LOBBY.SETTINGS.SLOT", DataType.INT, "7"),
    LOBBY_SETTINGS_ENABLED("ITEMS.IN_LOBBY.SETTINGS.ENABLED", DataType.BOOLEAN, "true"),
    LOBBY_STATS_NAME("ITEMS.IN_LOBBY.STATS.NAME", DataType.STRING, "&eYour Stats &7(Right Click)"),
    LOBBY_STATS_MATERIAL("ITEMS.IN_LOBBY.STATS.MATERIAL", DataType.STRING, "PLAYER_HEAD"),
    LOBBY_STATS_LORE("ITEMS.IN_LOBBY.STATS.LORE", DataType.STRING_LIST, " "),
    LOBBY_STATS_SLOT("ITEMS.IN_LOBBY.STATS.SLOT", DataType.INT, "8"),
    LOBBY_STATS_ENABLED("ITEMS.IN_LOBBY.STATS.ENABLED", DataType.BOOLEAN, "true"),
    LOBBY_DIVISIONS_NAME("ITEMS.IN_LOBBY.DIVISIONS.NAME", DataType.STRING, "&eView Divisions &7(Right Click)"),
    LOBBY_DIVISIONS_MATERIAL("ITEMS.IN_LOBBY.DIVISIONS.MATERIAL", DataType.STRING, "EMERALD"),
    LOBBY_DIVISIONS_LORE("ITEMS.IN_LOBBY.DIVISIONS.LORE", DataType.STRING_LIST, " "),
    LOBBY_DIVISIONS_SLOT("ITEMS.IN_LOBBY.DIVISIONS.SLOT", DataType.INT, "6"),
    LOBBY_DIVISIONS_ENABLED("ITEMS.IN_LOBBY.DIVISIONS.ENABLED", DataType.BOOLEAN, "true"),
    IN_QUEUE_QUEUE_LEAVE_NAME("ITEMS.IN_QUEUE.QUEUE_LEAVE.NAME", DataType.STRING, "&cLeave Queue &7(Right Click)"),
    IN_QUEUE_QUEUE_LEAVE_MATERIAL("ITEMS.IN_QUEUE.QUEUE_LEAVE.MATERIAL", DataType.STRING, "RED_DYE"),
    IN_QUEUE_QUEUE_LEAVE_LORE("ITEMS.IN_QUEUE.QUEUE_LEAVE.LORE", DataType.STRING_LIST, " "),
    IN_QUEUE_QUEUE_LEAVE_SLOT("ITEMS.IN_QUEUE.QUEUE_LEAVE.SLOT", DataType.INT, "8"),
    IN_QUEUE_QUEUE_LEAVE_ENABLED("ITEMS.IN_QUEUE.QUEUE_LEAVE.ENABLED", DataType.BOOLEAN, "true"),
    IN_QUEUE_LEADERBOARDS_NAME("ITEMS.IN_QUEUE.LEADERBOARDS.NAME", DataType.STRING, "&9Leaderboards &7(Right Click)"),
    IN_QUEUE_LEADERBOARDS_MATERIAL("ITEMS.IN_QUEUE.LEADERBOARDS.MATERIAL", DataType.STRING, "NETHER_STAR"),
    IN_QUEUE_LEADERBOARDS_LORE("ITEMS.IN_QUEUE.LEADERBOARDS.LORE", DataType.STRING_LIST, " "),
    IN_QUEUE_LEADERBOARDS_SLOT("ITEMS.IN_QUEUE.LEADERBOARDS.SLOT", DataType.INT, "4"),
    IN_QUEUE_LEADERBOARDS_ENABLED("ITEMS.IN_QUEUE.LEADERBOARDS.ENABLED", DataType.BOOLEAN, "true"),
    IN_QUEUE_STATS_NAME("ITEMS.IN_QUEUE.STATS.NAME", DataType.STRING, "&eYour Stats &7(Right Click)"),
    IN_QUEUE_STATS_MATERIAL("ITEMS.IN_QUEUE.STATS.MATERIAL", DataType.STRING, "PLAYER_HEAD"),
    IN_QUEUE_STATS_LORE("ITEMS.IN_QUEUE.STATS.LORE", DataType.STRING_LIST, " "),
    IN_QUEUE_STATS_SLOT("ITEMS.IN_QUEUE.STATS.SLOT", DataType.INT, "0"),
    IN_QUEUE_STATS_ENABLED("ITEMS.IN_QUEUE.STATS.ENABLED", DataType.BOOLEAN, "true"),
    IN_PARTY_PARTY_INFO_NAME("ITEMS.IN_PARTY.PARTY_INFO.NAME", DataType.STRING, "&aParty Information &7(Right Click)"),
    IN_PARTY_PARTY_INFO_MATERIAL("ITEMS.IN_PARTY.PARTY_INFO.MATERIAL", DataType.STRING, "COMPASS"),
    IN_PARTY_PARTY_INFO_LORE("ITEMS.IN_PARTY.PARTY_INFO.LORE", DataType.STRING_LIST, " "),
    IN_PARTY_PARTY_INFO_SLOT("ITEMS.IN_PARTY.PARTY_INFO.SLOT", DataType.INT, "0"),
    IN_PARTY_PARTY_INFO_ENABLED("ITEMS.IN_PARTY.PARTY_INFO.ENABLED", DataType.BOOLEAN, "true"),
    IN_PARTY_PARTY_SETTINGS_NAME("ITEMS.IN_PARTY.PARTY_SETTINGS.NAME", DataType.STRING, "&eParty Settings &7(Right Click)"),
    IN_PARTY_PARTY_SETTINGS_MATERIAL("ITEMS.IN_PARTY.PARTY_SETTINGS.MATERIAL", DataType.STRING, "PAPER"),
    IN_PARTY_PARTY_SETTINGS_LORE("ITEMS.IN_PARTY.PARTY_SETTINGS.LORE", DataType.STRING_LIST, " "),
    IN_PARTY_PARTY_SETTINGS_SLOT("ITEMS.IN_PARTY.PARTY_SETTINGS.SLOT", DataType.INT, "1"),
    IN_PARTY_PARTY_SETTINGS_ENABLED("ITEMS.IN_PARTY.PARTY_SETTINGS.ENABLED", DataType.BOOLEAN, "true"),
    IN_PARTY_PARTY_EVENTS_NAME("ITEMS.IN_PARTY.PARTY_EVENTS.NAME", DataType.STRING, "&dParty Events &7(Right Click)"),
    IN_PARTY_PARTY_EVENTS_MATERIAL("ITEMS.IN_PARTY.PARTY_EVENTS.MATERIAL", DataType.STRING, "NAME_TAG"),
    IN_PARTY_PARTY_EVENTS_LORE("ITEMS.IN_PARTY.PARTY_EVENTS.LORE", DataType.STRING_LIST, " "),
    IN_PARTY_PARTY_EVENTS_SLOT("ITEMS.IN_PARTY.PARTY_EVENTS.SLOT", DataType.INT, "2"),
    IN_PARTY_PARTY_EVENTS_ENABLED("ITEMS.IN_PARTY.PARTY_EVENTS.ENABLED", DataType.BOOLEAN, "true"),
    IN_PARTY_PARTY_DUEL_NAME("ITEMS.IN_PARTY.PARTY_DUEL.NAME", DataType.STRING, "&6Party Duel &7(Right Click)"),
    IN_PARTY_PARTY_DUEL_MATERIAL("ITEMS.IN_PARTY.PARTY_DUEL.MATERIAL", DataType.STRING, "DIAMOND_SWORD"),
    IN_PARTY_PARTY_DUEL_SLOT("ITEMS.IN_PARTY.PARTY_DUEL.SLOT", DataType.INT, "3"),
    IN_PARTY_PARTY_DUEL_ENABLED("ITEMS.IN_PARTY.PARTY_DUEL.ENABLED", DataType.BOOLEAN, "true"),
    IN_PARTY_PARTY_DISBAND_NAME("ITEMS.IN_PARTY.PARTY_DISBAND.NAME", DataType.STRING, "&cLeave Party &7(Right Click)"),
    IN_PARTY_PARTY_DISBAND_MATERIAL("ITEMS.IN_PARTY.PARTY_DISBAND.MATERIAL", DataType.STRING, "RED_DYE"),
    IN_PARTY_PARTY_DISBAND_LORE("ITEMS.IN_PARTY.PARTY_DISBAND.LORE", DataType.STRING_LIST, " "),
    IN_PARTY_PARTY_DISBAND_SLOT("ITEMS.IN_PARTY.PARTY_DISBAND.SLOT", DataType.INT, "8"),
    IN_PARTY_PARTY_DISBAND_ENABLED("ITEMS.IN_PARTY.PARTY_DISBAND.ENABLED", DataType.BOOLEAN, "true"),

    LOBBY_UNRANKED_CUSTOM_MODEL_DATA("ITEMS.LOBBY.UNRANKED.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    LOBBY_SPECTATE_MENU_CUSTOM_MODEL_DATA("ITEMS.LOBBY.SPECTATE.MENU.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    LOBBY_LEADERBOARDS_CUSTOM_MODEL_DATA("ITEMS.LOBBY.LEADERBOARDS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    LOBBY_KIT_EDITOR_CUSTOM_MODEL_DATA("ITEMS.LOBBY.KIT.EDITOR.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_GAME_PLAY_AGAIN_CUSTOM_MODEL_DATA("ITEMS.IN.GAME.PLAY.AGAIN.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_GAME_REMATCH_CUSTOM_MODEL_DATA("ITEMS.IN.GAME.REMATCH.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    LOBBY_PARTY_CREATE_CUSTOM_MODEL_DATA("ITEMS.LOBBY.PARTY.CREATE.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    LOBBY_SETTINGS_CUSTOM_MODEL_DATA("ITEMS.LOBBY.SETTINGS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    LOBBY_STATS_CUSTOM_MODEL_DATA("ITEMS.LOBBY.STATS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    LOBBY_DIVISIONS_CUSTOM_MODEL_DATA("ITEMS.LOBBY.DIVISIONS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_QUEUE_QUEUE_LEAVE_CUSTOM_MODEL_DATA("ITEMS.IN.QUEUE.QUEUE.LEAVE.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_QUEUE_LEADERBOARDS_CUSTOM_MODEL_DATA("ITEMS.IN.QUEUE.LEADERBOARDS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_QUEUE_STATS_CUSTOM_MODEL_DATA("ITEMS.IN.QUEUE.STATS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_PARTY_PARTY_INFO_CUSTOM_MODEL_DATA("ITEMS.IN.PARTY.PARTY.INFO.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_PARTY_PARTY_SETTINGS_CUSTOM_MODEL_DATA("ITEMS.IN.PARTY.PARTY.SETTINGS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_PARTY_PARTY_EVENTS_CUSTOM_MODEL_DATA("ITEMS.IN.PARTY.PARTY.EVENTS.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_PARTY_PARTY_DUEL_CUSTOM_MODEL_DATA("ITEMS.IN.PARTY.PARTY.DUEL.CUSTOM_MODEL_DATA", DataType.INT, "0"),
    IN_PARTY_PARTY_DISBAND_CUSTOM_MODEL_DATA("ITEMS.IN.PARTY.PARTY.DISBAND.CUSTOM_MODEL_DATA", DataType.INT, "0");


    private final String path;
    private final String comment;
    private final List<String> defaultValue = new ArrayList<>();
    private final DataType dataType;

    HotbarLocale(String path, DataType dataType, String... defaultValue) {
        this.path = path;
        this.comment = null;
        this.defaultValue.addAll(Arrays.asList(defaultValue));
        this.dataType = dataType;
    }

    @Override
    public String getHeader() {
        return "PLAYER_HEAD supported";
    }

    @Override
    public ConfigFile getConfigFile() {
        return ConfigService.get().getHotbarConfig();
    }
}
