package net.Boster.particles.main.data.extensions;

import net.Boster.particles.main.data.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface KillEffects {

    void onKill(@NotNull PlayerData killer, @NotNull Player killed);
}
