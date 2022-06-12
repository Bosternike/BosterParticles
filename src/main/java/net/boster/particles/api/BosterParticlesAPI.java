package net.boster.particles.api;

import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.particle.BosterParticle;
import net.boster.particles.main.particle.EnumBosterParticle;
import net.boster.particles.main.particle.blockdata.BPBlockData;
import net.boster.particles.main.particle.blockdata.BlockDataUtils;
import net.boster.particles.main.particle.dust.BPDustOptions;
import net.boster.particles.main.particle.dust.DustOptionsUtils;
import net.boster.particles.main.trail.CraftTrail;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BosterParticlesAPI {

    public static PlayerData getPlayer(@NotNull Player p) {
        return PlayerData.get(p);
    }

    public static void registerTrailsExtension(@NotNull Class<? extends CraftTrail> clazz) {
        CraftTrail.register(clazz);
    }

    public static boolean registerPlayerDataExtension(@NotNull String id, @NotNull Class<? extends PlayerDataExtension> clazz) {
        return PlayerData.registerExtension(id, clazz);
    }

    public static BosterParticle createParticle(@NotNull EnumBosterParticle particle) {
        return new BosterParticle(particle);
    }

    public static BosterParticle createParticle(@NotNull EnumBosterParticle particle, @Nullable BPDustOptions dustOptions) {
        return new BosterParticle(particle, dustOptions);
    }

    public static BosterParticle createParticle(@NotNull EnumBosterParticle particle, @Nullable BPBlockData blockData) {
        return new BosterParticle(particle, blockData);
    }

    public static @NotNull BPDustOptions createDustOptions(int r, int g, int b, int amount) {
        return DustOptionsUtils.create(r, g, b, amount);
    }

    public static @Nullable BPBlockData createBlockData(@NotNull Material material) {
        return BlockDataUtils.createBlockData(material);
    }
}
