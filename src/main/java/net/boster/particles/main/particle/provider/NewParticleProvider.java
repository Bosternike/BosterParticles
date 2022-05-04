package net.boster.particles.main.particle.provider;

import net.boster.particles.main.particle.EnumBosterParticle;
import net.boster.particles.main.particle.ParticleProvider;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class NewParticleProvider implements ParticleProvider {

    @Override
    public <T> void play(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options, T t) {
        loc.getWorld().spawnParticle((Particle) particle, loc, amount, x, y, z, options, t);
    }

    @Override
    public void play(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options) {
        loc.getWorld().spawnParticle((Particle) particle, loc, amount, x, y, z, options);
    }

    @Override
    public Object prepareObject(@NotNull EnumBosterParticle particle) {
        return Particle.valueOf(particle.name());
    }
}
