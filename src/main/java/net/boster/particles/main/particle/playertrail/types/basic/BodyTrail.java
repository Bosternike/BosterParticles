package net.boster.particles.main.particle.playertrail.types.basic;

import lombok.Getter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.particle.playertrail.CraftPlayerTrail;
import net.boster.particles.main.particle.playertrail.PlayerTrailType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class BodyTrail extends PlayerTrailType {

    @Getter private final double height;

    public BodyTrail(@NotNull String id, double height) {
        super(id);
        this.height = height;
        register();
    }

    @Override
    public void run(@NotNull CraftPlayerTrail trail) {
        trail.data.otherTasks.add(new BukkitRunnable() {
            @Override
            public void run() {
                if(!trail.data.getPlayer().isOnline()) {
                    this.cancel();
                    return;
                }

                if(trail.data.isMoving() && trail.stopOnMove) {
                    return;
                }

                trail.particle.spawn(trail.data.getPlayer().getLocation().add(0, height, 0));
            }
        }.runTaskTimer(BosterParticles.getInstance(), 0, trail.delay));
    }
}
