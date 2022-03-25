package net.boster.particles.main.gui;

import org.bukkit.entity.Player;

public interface GUIButton {

    int getSlot();

    void onClick(Player p);
}
