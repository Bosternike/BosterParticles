package net.boster.particles.main.nms.v1_8_R2;

import net.boster.particles.main.nms.EntityItemNMS;
import net.minecraft.server.v1_8_R2.EntityItem;
import net.minecraft.server.v1_8_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NMSEntityItem implements EntityItemNMS {

    @Override
    public Item create(@NotNull Location loc, @NotNull ItemStack item) {
        World w = ((CraftWorld) loc.getWorld()).getHandle();
        EntityItem i = new EntityItem(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), CraftItemStack.asNMSCopy(item)) {
            @Override
            public void t_() {}
        };
        w.addEntity(i);
        return (Item) i.getBukkitEntity();
    }
}
