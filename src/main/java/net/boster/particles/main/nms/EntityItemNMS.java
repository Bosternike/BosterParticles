package net.boster.particles.main.nms;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface EntityItemNMS {

    Item create(@NotNull Location loc, @NotNull ItemStack item);
}
