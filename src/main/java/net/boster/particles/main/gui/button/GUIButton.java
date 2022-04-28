package net.boster.particles.main.gui.button;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface GUIButton {

    int getSlot();

    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    default void onClick(Player p) {

    }

    default void onLeftClick(Player p) {
        onClick(p);
    }

    default void onRightClick(Player p) {
        onClick(p);
    }

    @Nullable ItemStack prepareItem(Player p);
}
