package net.boster.particles.main.trail.playertrail.types.limiter;

import net.boster.particles.main.trail.playertrail.CraftPlayerTrail;
import org.jetbrains.annotations.NotNull;

public interface HistoryLimiter {

    void checkLimit(@NotNull CraftPlayerTrail history);
}
