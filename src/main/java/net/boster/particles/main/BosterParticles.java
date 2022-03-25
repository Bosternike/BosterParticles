package net.boster.particles.main;

import net.boster.particles.main.commands.Commands;
import net.boster.particles.main.data.FileManager;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.DatabaseRunnable;
import net.boster.particles.main.data.database.setter.DataSetter;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.data.extensions.PlayerTrailExtension;
import net.boster.particles.main.lib.PAPISupport;
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

    private static BosterParticles instance;

    public String PREFIX = "\u00a76+\u00a7a---------------- \u00a7dBosterParticles \u00a7a------------------\u00a76+";

    private BPLoader loader;
    private FileManager fileManager;
    private DataSetter dataSetter;

    public void onEnable() {
        instance = this;

        DatabaseRunnable.enable();
        PAPISupport.load();
        registerPlayerDataExtension("player_data_extension", PlayerTrailExtension.class);

        loader = new BPLoader(this);
        fileManager = new FileManager(this);

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new Events(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getCommand("bosterparticles").setExecutor(new Commands(this));

        Bukkit.getConsoleSender().sendMessage(PREFIX);
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
    }

    public void enabledUpdater() {
        new UpdateChecker(this, 100933).getVersion(version -> {
            if(this.getDescription().getVersion().equals(version)) {
                log("&6UPDATER: &7There is no new update available.", LogType.INFO);
            } else {
                log("&6UPDATER: &7There is a new update available.", LogType.INFO);
                log("&6UPDATER: &7Download the new version:&a https://www.spigotmc.org/resources/bosterparticles.100933/", LogType.INFO);
            }
        });
    }

    public static BosterParticles getInstance() {
        return instance;
    }

    public void setDataSetter(DataSetter setter) {
        if(setter == null) {
            log("DataSetter can not be null", LogType.ERROR);
            return;
        }

        dataSetter = setter;
    }

    public DataSetter getDataSetter() {
        return dataSetter;
    }

    public static String toColor(String s) {
        return Utils.toColor(s);
    }

    public BPLoader getLoader() {
        return loader;
    }

    public boolean isOldVersion() {
        return loader.isOldVersion();
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void log(String s, LogType log) {
        Bukkit.getConsoleSender().sendMessage(log.getFormat() + log.getColor() + toColor(s));
    }

    public static void registerTrailsExtension(@NotNull Class<? extends CraftTrail> clazz) {
        CraftTrail.register(clazz);
    }

    public static boolean registerPlayerDataExtension(@NotNull String id, @NotNull Class<? extends PlayerDataExtension> clazz) {
        return PlayerData.registerExtension(id, clazz);
    }
}
