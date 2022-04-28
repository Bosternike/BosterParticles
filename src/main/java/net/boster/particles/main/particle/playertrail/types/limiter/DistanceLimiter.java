package net.boster.particles.main.particle.playertrail.types.limiter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.particles.main.particle.playertrail.CraftPlayerTrail;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DistanceLimiter implements HistoryLimiter {

    @Getter private final double distance;

    @Override
    public void checkLimit(@NotNull CraftPlayerTrail history) {
        if(!history.p.isOnline()) {
            history.getHistory().clear();
            return;
        }

        history.getHistory().removeIf(loc -> history.p.getWorld() != loc.getWorld() || history.p.getLocation().distance(loc) > distance);
    }
}
