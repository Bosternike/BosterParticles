package net.boster.particles.main.particle.dust;

import org.jetbrains.annotations.NotNull;

public class OldDustOptionsProvider implements DustOptionsProvider {

    @Override
    public @NotNull BPDustOptions create(int r, int g, int b, float amount) {
        return () -> new int[]{r, g, b};
    }
}
