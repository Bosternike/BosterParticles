package net.boster.particles.main.particle.playertrail.types;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.particle.playertrail.CraftPlayerTrail;
import net.boster.particles.main.particle.playertrail.PlayerTrailType;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class CirculatingRing extends PlayerTrailType {

    public CirculatingRing() {
        super("CIRCULATING_RING");
        register();
    }

    @Override
    public void run(@NotNull CraftPlayerTrail trail) {
        trail.data.otherTasks.add(new BukkitRunnable() {
            double deg = 0;
            @Override
            public void run() {
                if(!trail.data.getPlayer().isOnline()) {
                    this.cancel();
                    return;
                }
                if(trail.data.isMoving() && trail.stopOnMove) {
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
}
