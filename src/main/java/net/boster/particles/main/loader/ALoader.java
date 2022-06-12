package net.boster.particles.main.loader;

import net.boster.particles.main.BosterParticles;
import org.jetbrains.annotations.NotNull;

public abstract class ALoader {

    @NotNull protected final BPLoader loader;
    @NotNull protected final BosterParticles plugin;

    public ALoader(@NotNull BPLoader loader, @NotNull BosterParticles plugin) {
        this.loader = loader;
        this.plugin = plugin;
    }
}
