package net.Boster.particles.main;

import net.Boster.particles.main.commands.Commands;
import net.Boster.particles.main.data.FileManager;
import net.Boster.particles.main.data.PlayerData;
import net.Boster.particles.main.data.database.DatabaseRunnable;
import net.Boster.particles.main.data.database.setter.DataSetter;
import net.Boster.particles.main.data.extensions.PlayerDataExtension;
import net.Boster.particles.main.lib.PAPISupport;
import net.Boster.particles.main.listeners.Events;
import net.Boster.particles.main.listeners.InventoryListener;
import net.Boster.particles.main.particle.CraftTrail;
import net.Boster.particles.main.utils.LogType;
import net.Boster.particles.main.utils.Utils;
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

        loader = new BPLoader(this);
        fileManager = new FileManager(this);

        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new Events(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

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
