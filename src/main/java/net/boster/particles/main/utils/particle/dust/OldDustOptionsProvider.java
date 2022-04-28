package net.boster.particles.main.utils.particle.dust;

import org.jetbrains.annotations.NotNull;

public class OldDustOptionsProvider implements DustOptionsProvider {

    @Override
    public @NotNull Object create(int r, int g, int b, int amount) {
        return 1;
    }
}
