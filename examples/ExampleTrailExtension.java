package examples;

import net.Boster.particles.main.data.PlayerData;
import net.Boster.particles.main.particle.CraftTrail;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ExampleTrailExtension extends CraftTrail {

    public boolean hasParticle = false;

    /**
     * This method is used to load player's trails.
     */
    public static void load(PlayerData data) {
        Player p = data.p;
        FileConfiguration file = data.data;
        ConfigurationSection particles = file.getConfigurationSection("Particles");
        if(particles != null && particles.getKeys(false).size() > 0) {
            for(String pp : particles.getKeys(false)) {
                ExampleTrailExtension e = new ExampleTrailExtension();
                e.hasParticle = particles.getBoolean(pp + ".hasParticle", false);
                data.trails.add(e);
            }
        }
    }

    @Override
    public void spawn(Location loc) {
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 1, 0, 0, 0, 0);
    }
}
