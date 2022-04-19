package net.boster.particles.main.particle.playertrail.types.limiter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.particles.main.particle.playertrail.CraftPlayerTrail;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class LocationsLimiter implements HistoryLimiter {

    @Getter private final int amount;

    @Override
    public void checkLimit(@NotNull CraftPlayerTrail history) {
        if(!history.p.isOnline()) {
            history.getHistory().clear();
            return;
        }

        if(history.getHistory().size() > amount && history.getHistory().size() >= 2) {
            history.setHistory(history.getHistory().subList(1, history.getHistory().size()));
        }
    }
}
