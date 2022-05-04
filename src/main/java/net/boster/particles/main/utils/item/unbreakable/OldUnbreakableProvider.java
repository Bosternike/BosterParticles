package net.boster.particles.main.utils.item.unbreakable;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class OldUnbreakableProvider implements UnbreakableProvider {

    @Override
    public void setUnbreakable(@NotNull ItemMeta meta, boolean b) {
        try {
            Object spigot = meta.getClass().getMethod("spigot").invoke(meta);
            spigot.getClass().getMethod("setUnbreakable", boolean.class).invoke(spigot, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
