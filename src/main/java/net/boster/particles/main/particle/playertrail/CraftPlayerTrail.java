package net.boster.particles.main.particle.playertrail;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.extensions.PlayerTrailExtension;
import net.boster.particles.main.utils.BosterParticle;
import net.boster.particles.main.utils.LogType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CraftPlayerTrail {

    public final PlayerTrailExtension data;
    public final Player p;
    public final BosterParticle particle;
    public final PlayerTrailType type;
    public int delay = 0;
    public double radius = 0.5;

    public CraftPlayerTrail(@NotNull PlayerTrailExtension data, @NotNull BosterParticle particle, @NotNull PlayerTrailType type) {
        this.data = data;
        this.p = data.getPlayer();
        this.particle = particle;
        this.type = type;
    }

    public static void load(@NotNull PlayerTrailExtension data) {
        FileConfiguration file = data.getData().data;
        ConfigurationSection particles = file.getConfigurationSection("PlayerTrails");
        if(particles != null && particles.getKeys(false).size() > 0) {
            for(String pp : particles.getKeys(false)) {
                ConfigurationSection section = particles.getConfigurationSection(pp);
                BosterParticle bp = BosterParticle.load(section);
                if(bp != null) {
                    try {
                        CraftPlayerTrail cp = new CraftPlayerTrail(data, bp, PlayerTrailType.valueOf(section.getString("Figure")));
                        cp.delay = section.getInt("Delay", 0);
                        cp.radius = section.getDouble("Radius", 0.5);
                        data.trails.add(cp);
                    } catch (Exception e) {
                        BosterParticles.getInstance().log("&7Could not load Figure \"&c" + section.getString("Figure") + "&7\"", LogType.WARNING);
                    }
                }
            }
        }
    }

    public void start() {
        if(particle == null) {
            BosterParticles.getInstance().log("&7Could not spawn Particle because it's null.", LogType.ERROR);
            return;
        }

        type.run(this);
    }
}
