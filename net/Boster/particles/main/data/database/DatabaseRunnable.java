package net.Boster.particles.main.data.database;

import net.Boster.particles.main.BosterParticles;
import org.bukkit.Bukkit;

public class DatabaseRunnable {

    private static boolean isDisabled = true;

    public static void disable() {
        isDisabled = true;
    }

    public static void enable() {
        isDisabled = false;
    }

    private String returns;

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
