package net.boster.particles.main.utils.item.creator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class OldItemCreator implements ItemCreator {

    @Override
    public ItemStack createItem(String s) {
        try {
            String[] ss = s.split(":");
            return new ItemStack(Material.valueOf(ss[0]), 1, (short) Integer.parseInt(ss[1]));
        } catch (IndexOutOfBoundsException e) {
            return new ItemStack(Material.valueOf(s));
        }
    }
}
