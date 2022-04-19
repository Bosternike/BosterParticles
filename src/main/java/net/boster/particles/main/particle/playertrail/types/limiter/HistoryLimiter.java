package net.boster.particles.main.particle.playertrail.types.limiter;

import net.boster.particles.main.particle.playertrail.CraftPlayerTrail;
import org.jetbrains.annotations.NotNull;

public interface HistoryLimiter {

    void checkLimit(@NotNull CraftPlayerTrail history);
}
