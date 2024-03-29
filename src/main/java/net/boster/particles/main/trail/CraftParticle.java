package net.boster.particles.main.trail;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.particle.BosterParticle;
import net.boster.particles.main.utils.log.LogType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CraftParticle extends CraftTrail {

    public final Player p;
    public final String player;
    public final BosterParticle particle;

    public CraftParticle(@NotNull Player p, @NotNull BosterParticle particle) {
        this.p = p;
        this.player = p.getName();
        this.particle = particle;
    }

    public static void load(@NotNull PlayerData data) {
        Player p = data.getPlayer();
        FileConfiguration file = data.data;
        ConfigurationSection particles = file.getConfigurationSection("Particles");
        if(particles != null && particles.getKeys(false).size() > 0) {
            for(String pp : particles.getKeys(false)) {
                BosterParticle bp = BosterParticle.load(particles.getConfigurationSection(pp));
                if(bp != null) {
                    data.trails.add(new CraftParticle(p, bp));
                }
            }
        }
    }

    @Override
    public void spawn(@NotNull Location loc) {
        if(particle == null) {
            BosterParticles.getInstance().log("&7Could not spawn Particle because it's null. (Location = " + loc + "; Player = " + player + ")", LogType.ERROR);
            return;
        }

        try {
            particle.defaultSpawn(loc);
        } catch (Exception e) {
            BosterParticles.getInstance().log("&7Could not spawn BosterParticle \"&e" + particle + "&7\". (Location = " + loc + "; Player = " + player + ")", LogType.ERROR);
        }
    }
}
