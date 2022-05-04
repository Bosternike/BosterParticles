package net.boster.particles.main.utils.item.unbreakable;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NewUnbreakableProvider implements UnbreakableProvider {

    @Override
    public void setUnbreakable(@NotNull ItemMeta meta, boolean b) {
        try {
            meta.getClass().getMethod("setUnbreakable", boolean.class).invoke(meta, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
