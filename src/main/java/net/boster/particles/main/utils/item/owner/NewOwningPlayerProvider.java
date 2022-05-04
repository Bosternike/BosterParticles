package net.boster.particles.main.utils.item.owner;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class NewOwningPlayerProvider implements OwningPlayerProvider {

    @Override
    public void setOwner(@NotNull SkullMeta meta, @NotNull String player) {
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player));
    }
}
