package net.boster.particles.main.gui.multipage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface MultiPageEntry extends MultiPageFunctionalEntry {

    @Nullable ItemStack item(Player p);

    default @Nullable ItemStack item(Player p, int page, int slot) {
        return item(p);
    }

    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    default void onClick(MultiPageGUI gui, Player p) {

    }

    default void onLeftClick(MultiPageGUI gui, Player p) {
        onClick(gui, p);
    }

    default void onRightClick(MultiPageGUI gui, Player p) {
        onClick(gui, p);
    }

    default void onLeftClick(MultiPageGUI gui, Player p, int page, int slot) {
        onLeftClick(gui, p);
    }

    default void onRightClick(MultiPageGUI gui, Player p, int page, int slot) {
        onRightClick(gui, p);
    }
}
