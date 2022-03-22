package net.Boster.example;

import net.Boster.particles.main.data.PlayerData;
import net.Boster.particles.main.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExampleExtension extends PlayerDataExtension implements DeathEffects, KillEffects {

    private final boolean hasDeathEffect;
    private final boolean hasKillEffect;

    public ExampleExtension(@NotNull String id, @NotNull PlayerData data) {
        super(id, data);
        this.hasDeathEffect = data.data.getBoolean("hasDeathEffect", false);
        this.hasKillEffect = data.data.getBoolean("hasKillEffect", false);
    }

    @Override
    public void onDeath(@NotNull PlayerData data) {
        if(hasDeathEffect) {
            ParticleUtils.playCircularEffect(data.p.getLocation(), Particle.REDSTONE, new Particle.DustOptions(Color.PURPLE, 1));
        }
    }

    @Override
    public void onKill(@NotNull PlayerData killer, @NotNull Player killed) {
        if(hasKillEffect) {
            ParticleUtils.runDampenedRadialWaves(killed.getLocation());
        }
    }
}
