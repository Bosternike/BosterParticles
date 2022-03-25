package net.boster.particles.main.data.extensions;

import net.boster.particles.main.data.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface KillEffects {

    void onKill(@NotNull PlayerData killer, @NotNull Player killed);
}
