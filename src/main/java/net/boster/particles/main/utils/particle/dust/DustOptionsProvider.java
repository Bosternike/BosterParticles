package net.boster.particles.main.utils.particle.dust;

import org.jetbrains.annotations.NotNull;

public interface DustOptionsProvider {

    @NotNull Object create(int r, int g, int b, int amount);
}
