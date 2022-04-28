package net.boster.particles.main.gui.manage.chat;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ChatInputObserver {

    void onChat(@NotNull Player p, @NotNull String input);

    void cancel();
}
