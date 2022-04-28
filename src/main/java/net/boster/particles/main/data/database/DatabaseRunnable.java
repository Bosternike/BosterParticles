package net.boster.particles.main.data.database;

import net.boster.particles.main.BosterParticles;
import org.bukkit.Bukkit;

public class DatabaseRunnable {

    private static boolean isDisabled = true;
    private String returns;

    public static void disable() {
        isDisabled = true;
    }

    public static void enable() {
        isDisabled = false;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String s) {
        returns = s;
    }

    public void run(Runnable run) {
        if(!isDisabled) {
            Bukkit.getScheduler().runTaskAsynchronously(BosterParticles.getInstance(), run);
        } else {
            run.run();
        }
    }
}
