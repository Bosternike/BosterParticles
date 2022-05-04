package net.boster.particles.main.data.extensions;

import lombok.Getter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.trail.playertrail.CraftPlayerTrail;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerTrailExtension extends PlayerDataExtension {

    @Getter private BukkitTask trailTask;
    public final List<CraftPlayerTrail> trails = new ArrayList<>();
    @Getter public boolean moving = false;
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
            Location loc = player.getLocation();
            @Override
            public void run() {
                if(!player.isOnline()) {
                    this.cancel();
                    return;
                }

                if(loc.getWorld() == player.getLocation().getWorld() && loc.distance(player.getLocation()) <= 0.1) {
                    moving = false;
                } else {
                    loc = player.getLocation();
                    moving = true;
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
            task.cancel();
        }
        otherTasks.clear();
    }
}
