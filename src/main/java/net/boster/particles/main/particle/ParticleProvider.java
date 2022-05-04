package net.boster.particles.main.particle;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface ParticleProvider {

    <T> void play(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options, T t);
    void play(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options);

    Object prepareObject(@NotNull EnumBosterParticle particle);
}
