package net.boster.particles.main.api;

import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.trail.CraftTrail;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
}
