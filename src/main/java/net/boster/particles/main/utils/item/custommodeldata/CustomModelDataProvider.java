package net.boster.particles.main.utils.item.custommodeldata;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public interface CustomModelDataProvider {

    void setCustomModelData(@NotNull ItemMeta meta, int i);
}
