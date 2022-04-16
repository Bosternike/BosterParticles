package net.boster.particles.main.data.extensions;

import net.boster.particles.main.data.PlayerData;
import org.jetbrains.annotations.NotNull;

public interface DeathEffects {

    void onDeath(@NotNull PlayerData data);
}
