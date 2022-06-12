package net.boster.particles.main.nms;

import net.boster.particles.main.utils.Version;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NMSProvider {

    public static final String NO_MERGE = "§bBosterParticles §eitem, that can't be merged!";

    private static EntityItemNMS entityItemNMS;

    public static void load() {
        Object o = loadNMSClass("NMSEntityItem", 4);
        if(o != null) {
            entityItemNMS = (EntityItemNMS) o;
        }
    }

    private static Object loadNMSClass(String name, int i) {
        if(Version.getCurrentVersion().getVersionInteger() >= i) return null;

        try {
            return Class.forName("net.boster.particles.main.nms." + Version.getCurrentVersion().name() + "." + name).getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    public static Item createItem(@NotNull Location loc, @NotNull ItemStack item) {
        if(entityItemNMS != null) {
            return entityItemNMS.create(loc, item);
        } else {
            return setMergeLess(loc.getWorld().dropItemNaturally(loc, item.clone()));
        }
    }

    public static Item setMergeLess(@NotNull Item ent) {
        ent.setCustomNameVisible(false);
        ent.setCustomName(NO_MERGE);
        return ent;
    }
}
