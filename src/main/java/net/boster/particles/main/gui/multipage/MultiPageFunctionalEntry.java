package net.boster.particles.main.gui.multipage;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface MultiPageFunctionalEntry {

    @Nullable ItemStack item(Player p, int page, int slot);

    void onLeftClick(MultiPageGUI gui, Player p, int page, int slot);

    void onRightClick(MultiPageGUI gui, Player p, int page, int slot);
}
