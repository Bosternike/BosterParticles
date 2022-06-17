package net.boster.particles.main.particle.dust;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class NewDustOptionsProvider implements DustOptionsProvider {

    @Override
    public @NotNull BPDustOptions create(int r, int g, int b, float amount) {
        Color c = Color.fromRGB(r, g, b);
        return () -> new Particle.DustOptions(c, amount);
    }
}
