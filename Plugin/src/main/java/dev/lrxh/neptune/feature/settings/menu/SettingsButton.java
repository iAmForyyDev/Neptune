package dev.lrxh.neptune.feature.settings.menu;

import dev.lrxh.neptune.API;
import dev.lrxh.neptune.feature.settings.Setting;
import dev.lrxh.neptune.profile.impl.Profile;
import dev.lrxh.neptune.providers.clickable.Replacement;
import dev.lrxh.neptune.utils.ItemBuilder;
import dev.lrxh.neptune.utils.ItemUtils;
import dev.lrxh.neptune.utils.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SettingsButton extends Button {
    private final Setting setting;

    public SettingsButton(int slot, Setting setting) {
        super(slot);
        this.setting = setting;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        Profile profile = API.getProfile(player);

        return new ItemBuilder(setting.getMaterial(), player.getUniqueId())
                .name(setting.getDisplayName())
                .lore(ItemUtils.getLore(setting.toggled(player) ? setting.getEnabledLore() : setting.getDisabledLore(),
                        new Replacement("<ping>", String.valueOf(profile.getSettingData().getMaxPing())),
                        new Replacement("<kill-effect>", profile.getSettingData().getKillEffect().getDisplayName()),
                        new Replacement("<kill-message>", profile.getSettingData().getKillMessagePackage().getDisplayName())), player)

                .build();
    }

    @Override
    public void onClick(ClickType type, Player player) {
        setting.execute(player, type);
    }
}
