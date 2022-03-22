package net.Boster.particles.main.data.extensions;

import net.Boster.particles.main.data.PlayerData;
import org.jetbrains.annotations.NotNull;

public interface DeathEffects {

    void onDeath(@NotNull PlayerData data);
}
