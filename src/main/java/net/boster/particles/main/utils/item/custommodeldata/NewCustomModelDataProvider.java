package net.boster.particles.main.utils.item.custommodeldata;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NewCustomModelDataProvider implements CustomModelDataProvider {

    @Override
    public void setCustomModelData(@NotNull ItemMeta meta, int i) {
        meta.setCustomModelData(i);
    }
}
