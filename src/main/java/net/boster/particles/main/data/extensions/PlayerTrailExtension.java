package net.boster.particles.main.data.extensions;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.particle.CraftPlayerTrail;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerTrailExtension extends PlayerDataExtension {

    public BukkitTask trailTask;
    public final List<CraftPlayerTrail> trails = new ArrayList<>();
    public boolean isMoving = false;
    public final List<BukkitTask> otherTasks = new ArrayList<>();

    public PlayerTrailExtension(@NotNull String id, @NotNull PlayerData data) {
        super(id, data);
        CraftPlayerTrail.load(this);
        startTrailTask();
    }

    public void stopTrailTask() {
        if(trailTask != null && !trailTask.isCancelled()) {
            trailTask.cancel();
        }
    }

    public void startTrailTask() {
        stopTrailTask();

        trailTask = new BukkitRunnable() {
            Location loc = p.getLocation();
            @Override
            public void run() {
                if(!p.isOnline()) {
                    this.cancel();
                    return;
                }

                if(loc.getWorld() == p.getLocation().getWorld() && loc.distance(p.getLocation()) <= 0.1) {
                    isMoving = false;
                } else {
                    loc = p.getLocation();
                    isMoving = true;
                }
            }
        }.runTaskTimer(BosterParticles.getInstance(), 0, 3);
        for(CraftPlayerTrail t : trails) {
            t.start();
        }
    }

    @Override
    public void onUpdate() {
        clearOtherTasks();
    }

    public void clearOtherTasks() {
        for(BukkitTask task : otherTasks) {
            if(!task.isCancelled()) {
                task.cancel();
            }
        }
        otherTasks.clear();
    }
}
