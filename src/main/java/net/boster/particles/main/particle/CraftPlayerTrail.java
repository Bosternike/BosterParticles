package net.boster.particles.main.particle;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.extensions.PlayerTrailExtension;
import net.boster.particles.main.utils.BosterParticle;
import net.boster.particles.main.utils.LogType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CraftPlayerTrail {

    public final PlayerTrailExtension data;
    public final Player p;
    public final BosterParticle particle;
    public final PlayerTrailType type;
    public int delay = 0;
    public double radius = 0.5;

    public CraftPlayerTrail(PlayerTrailExtension data, BosterParticle particle, PlayerTrailType type) {
        this.data = data;
        this.p = data.getPlayer();
        this.particle = particle;
        this.type = type;
    }

    public static void load(PlayerTrailExtension data) {
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

    public enum PlayerTrailType {
        CIRCULATING_RING {
            public void run(CraftPlayerTrail trail) {
                trail.data.otherTasks.add(new BukkitRunnable() {
                    double deg = 0;
                    @Override
                    public void run() {
                        if(!trail.data.getPlayer().isOnline()) {
                            this.cancel();
                            return;
                        }
                        if(trail.data.isMoving) {
                            return;
                        }
                        if(deg > 360) {
                            deg = 0;
                        }

                        Location aloc = trail.data.getPlayer().getLocation().add(0, 2.1, 0);
                        double radians = Math.toRadians(deg);
                        double dx = Math.cos(radians);
                        double dz = Math.sin(radians);
                        dx *= trail.radius;
                        dz *= trail.radius;
                        aloc.add(dx, 0, dz);
                        trail.particle.spawn(aloc);
                        aloc.subtract(dx, 0, dz);
                        deg += 10;
                    }
                }.runTaskTimer(BosterParticles.getInstance(), 0, trail.delay));
            }
        },
        MONO_RING {
            public void run(CraftPlayerTrail trail) {
                trail.data.otherTasks.add(new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!trail.data.getPlayer().isOnline()) {
                            this.cancel();
                            return;
                        }
                        if(trail.data.isMoving) {
                            return;
                        }

                        Location aloc = trail.data.getPlayer().getLocation().add(0, 2.1, 0);
                        for(double degree = 0; degree < 360; degree++) {
                            double radians = Math.toRadians(degree);
                            double dx = Math.cos(radians);
                            double dz = Math.sin(radians);
                            dx *= trail.radius;
                            dz *= trail.radius;
                            aloc.add(dx, 0, dz);
                            trail.particle.spawn(aloc);
                            aloc.subtract(dx, 0, dz);
                        }
                    }
                }.runTaskTimer(BosterParticles.getInstance(), 0, trail.delay));
            }
        };

        public abstract void run(CraftPlayerTrail trail);
    }
}
