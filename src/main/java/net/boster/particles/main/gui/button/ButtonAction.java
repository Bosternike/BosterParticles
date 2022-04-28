package net.boster.particles.main.gui.button;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ButtonAction {

    void act(@NotNull Player p, @Nullable String... replaces);
}
