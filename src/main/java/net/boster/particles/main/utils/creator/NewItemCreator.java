package net.boster.particles.main.utils.creator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NewItemCreator implements ItemCreator {

    @Override
    public ItemStack createItem(String s) {
        return new ItemStack(Material.valueOf(s));
    }
}
