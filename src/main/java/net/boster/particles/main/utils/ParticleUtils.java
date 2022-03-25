package net.boster.particles.main.utils;

import net.boster.particles.main.BosterParticles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleUtils {

    public static void playCircularEffect(Location loc, BosterParticle particle) {
        if(particle == null) return;

        double radius = 5;

        for(double y = 5; y >= 0; y -= 0.007) {
            radius = y / 3;
            double x = radius * Math.cos(3 * y);
            double z = radius * Math.sin(3 * y);

            double y2 = 5 - y;

            particle.spawn(new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y2, loc.getZ() + z));
        }

        for(double y = 5; y >= 0; y -= 0.007) {
            radius = y / 3;
            double x = -(radius * Math.cos(3 * y));
            double z = -(radius * Math.sin(3 * y));

            double y2 = 5 - y;

            particle.spawn(new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y2, loc.getZ() + z));
        }
    }

    public static void spawnVerticalRing(Location loc, BosterParticle particle, double size) {
        if(particle == null) return;

        Location aloc = loc.clone();
        for(double degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double dy = Math.cos(radians);
            double dz = Math.sin(radians);
            dy *= size;
            dz *= size;
            aloc.add(0, dy, dz);
            particle.spawn(aloc);
            aloc.subtract(0, dy, dz);
        }
    }

    public static void spawnVerticalSkippedRing(Location loc, BosterParticle particle, double size, int skips) {
        if(particle == null) return;

        Location aloc = loc.clone();
        for(double degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double dy = Math.cos(radians);
            double dz = Math.sin(radians);
            dy *= size;
            dz *= size;
            aloc.add(0, dy, dz);
            particle.spawn(aloc);
            aloc.subtract(0, dy, dz);
            degree += skips;
        }
    }

    public static void spawnHorizontalSkippedRing(Location loc, BosterParticle particle, double size, int skips) {
        if(particle == null) return;

        Location aloc = loc.clone();
        for(double degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double dx = Math.cos(radians);
            double dz = Math.sin(radians);
            dx *= size;
            dz *= size;
            aloc.add(dx, 0, dz);
            particle.spawn(aloc);
            aloc.subtract(dx, 0, dz);
            degree += skips;
        }
    }

    public static void spawnHorizontalRing(Location loc, BosterParticle particle, double size) {
        if(particle == null) return;

        Location aloc = loc.clone();
        for(double degree = 0; degree < 360; degree++) {
            double radians = Math.toRadians(degree);
            double dx = Math.cos(radians);
            double dz = Math.sin(radians);
            dx *= size;
            dz *= size;
            aloc.add(dx, 0, dz);
            particle.spawn(aloc);
            aloc.subtract(dx, 0, dz);
        }
    }

    public static void runDampenedRadialWaves(Location location) {
        Location loc = location.clone();
        new BukkitRunnable() {
            double t = Math.PI / 4;

            public void run(){
                t += 0.1 * Math.PI;
                for(double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI / 32) {
                    double x = t * Math.cos(theta);
                    double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
                    double z = t * Math.sin(theta);
                    loc.add(x, y, z);
                    loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 0, 0, 0, 0, 1);
                    loc.subtract(x, y, z);

                    theta = theta + Math.PI / 64;

                    x = t * Math.cos(theta);
                    y = 2 * Math.exp(-0.1*t) * Math.sin(t) + 1.5;
                    z = t * Math.sin(theta);
                    loc.add(x, y, z);
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 0, 0, 0, 0, 1);
                    loc.subtract(x, y, z);
                }
                if(t > 20) {
                    this.cancel();
                }
            }

        }.runTaskTimer(BosterParticles.getInstance(), 0, 1);
    }
}
