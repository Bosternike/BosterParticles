package net.boster.particles.main;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.data.FileManager;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.DatabaseRunnable;
import net.boster.particles.main.data.database.setter.DataSetter;
import net.boster.particles.main.data.extensions.PlayerTrailExtension;
import net.boster.particles.main.lib.PAPISupport;
import net.boster.particles.main.lib.VaultSupport;
import net.boster.particles.main.listeners.Events;
import net.boster.particles.main.listeners.InventoryListener;
import net.boster.particles.main.listeners.merge.ItemMergeListener;
import net.boster.particles.main.listeners.pickup.NewPickupListener;
import net.boster.particles.main.listeners.pickup.OldPickupListener;
import net.boster.particles.main.nms.NMSProvider;
import net.boster.particles.main.utils.Version;
import net.boster.particles.main.utils.log.LogType;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BosterParticles extends JavaPlugin {

    @Getter private static BosterParticles instance;

    @Getter private BPLoader loader;
    @Getter private FileManager fileManager;
    @Getter @Setter @NotNull private DataSetter dataSetter;
    @Getter @Nullable private BukkitTask updaterTask;

    public void onEnable() {
        instance = this;

        String PREFIX = "\u00a76+\u00a7a---------------- \u00a7dBosterParticles \u00a7a------------------\u00a76+";

        if(Version.getCurrentVersion() == Version.OLD_VERSION) {
            Bukkit.getConsoleSender().sendMessage(PREFIX);
            log("Plugin could not be enabled!", LogType.ERROR);
            log("Your core version is too old!", LogType.ERROR);
            log("Version support starts from: &a1.8", LogType.ERROR);
            Bukkit.getConsoleSender().sendMessage(PREFIX);
            return;
        }

        Bukkit.getConsoleSender().sendMessage(PREFIX);

        NMSProvider.load();
        DatabaseRunnable.enable();
        PAPISupport.load();
        VaultSupport.load();
        PlayerData.registerExtension("player_trail_extension", PlayerTrailExtension.class);

        loader = new BPLoader(this);
        fileManager = new FileManager(this);

        saveDefaultConfig();

        registerListeners();

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

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new Events(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        if(Version.getCurrentVersion().getVersionInteger() < 8) {
            getServer().getPluginManager().registerEvents(new OldPickupListener(), this);
        } else {
            getServer().getPluginManager().registerEvents(new NewPickupListener(), this);
        }
        if(Version.getCurrentVersion().getVersionInteger() > 3) {
            getServer().getPluginManager().registerEvents(new ItemMergeListener(), this);
        }
    }

    public void enableUpdater(int delay) {
        if(updaterTask != null) {
            updaterTask.cancel();
        }

        UpdateChecker c = new UpdateChecker(this, 100933);
        updaterTask = getServer().getScheduler().runTaskTimer(this, () -> {
            c.getVersion(version -> {
                if(!getDescription().getVersion().equals(version)) {
                    log("There is a new update available!", LogType.UPDATER);
                    log("Current version:&c " + getDescription().getVersion(), LogType.UPDATER);
                    log("New version:&b " + version, LogType.UPDATER);
                    log("Download link:&a https://www.spigotmc.org/resources/bosterparticles.100933/", LogType.UPDATER);
                }
            });
        }, 0, delay * 20L);
    }

    public static String toColor(String s) {
        return Utils.toColor(s);
    }

    public void log(String s, LogType log) {
        if(log.isToggleAble() && !loader.enabledLoggers.contains(log)) return;

        Bukkit.getConsoleSender().sendMessage(log.getFormat() + log.getColor() + toColor(s));
    }
}
