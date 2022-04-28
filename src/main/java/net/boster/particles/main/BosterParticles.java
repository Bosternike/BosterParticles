package net.boster.particles.main;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.data.FileManager;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.DatabaseRunnable;
import net.boster.particles.main.data.database.setter.DataSetter;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.data.extensions.PlayerTrailExtension;
import net.boster.particles.main.lib.PAPISupport;
import net.boster.particles.main.lib.VaultSupport;
import net.boster.particles.main.listeners.Events;
import net.boster.particles.main.listeners.InventoryListener;
import net.boster.particles.main.particle.CraftTrail;
import net.boster.particles.main.utils.LogType;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BosterParticles extends JavaPlugin {

    @Getter private static BosterParticles instance;

    public String PREFIX = "\u00a76+\u00a7a---------------- \u00a7dBosterParticles \u00a7a------------------\u00a76+";

    @Getter private BPLoader loader;
    @Getter private FileManager fileManager;
    @Getter @Setter @NotNull private DataSetter dataSetter;

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(PREFIX);
        instance = this;

        DatabaseRunnable.enable();
        PAPISupport.load();
        VaultSupport.load();
        registerPlayerDataExtension("player_trail_extension", PlayerTrailExtension.class);

        loader = new BPLoader(this);
        fileManager = new FileManager(this);

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new Events(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        loader.load();
        Bukkit.getConsoleSender().sendMessage("\u00a7d[\u00a7bBosterParticles\u00a7d] \u00a7fThe plugin has been \u00a7dEnabled\u00a7f!");
        Bukkit.getConsoleSender().sendMessage("\u00a7d[\u00a7bBosterParticles\u00a7d] \u00a7fPlugin creator: \u00a7dBosternike");
        Bukkit.getConsoleSender().sendMessage("\u00a7d[\u00a7bBosterParticles\u00a7d] \u00a7fPlugin version: \u00a7d" + getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(PREFIX);
    }

    public void onDisable() {
        DatabaseRunnable.disable();
        for(Player p : Bukkit.getOnlinePlayers()) {
            PlayerData.get(p).saveData();
        }
        if(loader.getConnectedDatabase() != null) {
            loader.getConnectedDatabase().closeConnection();
        }
    }

    public void enabledUpdater() {
        UpdateChecker c = new UpdateChecker(this, 100933);
        getServer().getScheduler().runTaskTimer(this, () -> {
            c.getVersion(version -> {
                if(!getDescription().getVersion().equals(version)) {
                    log("There is a new update available!", LogType.UPDATER);
                    log("Current version:&c " + getDescription().getVersion(), LogType.UPDATER);
                    log("New version:&b " + version, LogType.UPDATER);
                    log("Download link:&a https://www.spigotmc.org/resources/bosterparticles.100933/", LogType.UPDATER);
                }
            });
        }, 0, 3600 * 20);
    }

    public static String toColor(String s) {
        return Utils.toColor(s);
    }

    public boolean isOldVersion() {
        return loader.isOldVersion();
    }

    public void log(String s, LogType log) {
        if(loader.enabledLoggers.contains(log)) {
            Bukkit.getConsoleSender().sendMessage(log.getFormat() + log.getColor() + toColor(s));
        }
    }

    public static void registerTrailsExtension(@NotNull Class<? extends CraftTrail> clazz) {
        CraftTrail.register(clazz);
    }

    public static boolean registerPlayerDataExtension(@NotNull String id, @NotNull Class<? extends PlayerDataExtension> clazz) {
        return PlayerData.registerExtension(id, clazz);
    }
}
