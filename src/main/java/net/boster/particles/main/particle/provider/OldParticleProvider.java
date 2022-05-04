package net.boster.particles.main.particle.provider;

import net.boster.particles.main.particle.EnumBosterParticle;
import net.boster.particles.main.particle.ParticleProvider;
import net.boster.particles.main.utils.ReflectionUtils;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class OldParticleProvider implements ParticleProvider {

    @Override
    public <T> void play(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options, T t) {
        ReflectionUtils.sendParticle(particle, loc, amount, x, y, z, options, 70);
    }

    @Override
    public void play(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options) {
        ReflectionUtils.sendParticle(particle, loc, amount, x, y, z, options, 70);
    }

    @Override
    public Object prepareObject(@NotNull EnumBosterParticle particle) {
        return ReflectionUtils.getEnumParticle(particle.name());
    }
}
