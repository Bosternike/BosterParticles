package net.boster.particles.main.loader;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.commands.BosterCommand;
import net.boster.particles.main.commands.Commands;
import net.boster.particles.main.data.EnumStorage;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.ConnectedDatabase;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.utils.ConfigValues;
import net.boster.particles.main.utils.CustomTrailsUtils;
import net.boster.particles.main.utils.log.LogType;
import net.boster.particles.main.utils.item.ItemManager;
import net.boster.particles.main.utils.item.ItemManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BPLoader {

    private final BosterParticles plugin;

    @Getter @NotNull private final FilesLoader filesLoader;
    @Getter @NotNull private final StorageLoader storageLoader;

    public int RunnableDelay = 0;
    public boolean LoadPlayerTrailsAsync = true;

    @Getter @Setter @NotNull private ItemManager itemManager;

    public final Set<LogType> enabledLoggers = new HashSet<>();

    @Getter @Setter @NotNull private String prefix = "BosterParticles";
    @Getter @Setter @NotNull private EnumStorage storage = EnumStorage.YAML;
    @Getter @Setter @Nullable private ConnectedDatabase connectedDatabase;

    public BPLoader(BosterParticles plugin) {
        this.plugin = plugin;
        this.filesLoader = new FilesLoader(this, plugin);
        this.storageLoader = new StorageLoader(this, plugin);
        this.itemManager = new ItemManagerImpl();
    }

    public void load() {
        plugin.getLocalesManager().load();

        new Commands(plugin);

        filesLoader.loadFiles();

        loadLoggers();

        filesLoader.loadMenusFiles();
        storageLoader.loadDataSetter();

        loadMenus();

        DataConverter.load();
        CustomTrailsUtils.load();
        ConfigValues.load(plugin.getConfig());

        plugin.getServer().getScheduler().runTaskLater(plugin, this::loadPlayers, 20);

        BosterCommand.load();
    }

    public void resetLoggers() {
        enabledLoggers.clear();

        enabledLoggers.addAll(Arrays.asList(LogType.values()));
    }

    public void loadLoggers() {
        enabledLoggers.clear();

        for(String l : plugin.getConfig().getStringList("Settings.EnabledLoggers")) {
            try {
                enabledLoggers.add(LogType.valueOf(l));
            } catch (Exception ignored) {}
        }
        if(plugin.getConfig().getBoolean("Updater.Enabled", false)) {
            plugin.enableUpdater(plugin.getConfig().getInt("Updater.Delay", 3600));
        }
    }

    public void loadMenus() {
        for(MenuFile file : MenuFile.files()) {
            new ParticlesGUI(file);
        }
    }

    public void loadPlayers() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            PlayerData.load(p);
        }
    }

    public void unLoadFiles() {
        MenuFile.clearAll();
    }

    public void unloadMenus() {
        ParticlesGUI.clearAll();
    }

    public void unloadPlayers() {
        PlayerData.clearAll();
    }

    public void reload() {
        BosterCommand.unload();
        unloadMenus();
        unLoadFiles();
        unloadPlayers();
        plugin.reloadConfig();
        enabledLoggers.clear();
        load();
    }
}
