package net.boster.particles.main;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.commands.BosterCommand;
import net.boster.particles.main.commands.Commands;
import net.boster.particles.main.data.EnumStorage;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.ConnectedDatabase;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.data.database.MySqlConnectionUtils;
import net.boster.particles.main.data.database.SQLiteConnectionUtils;
import net.boster.particles.main.data.database.setter.FileSetter;
import net.boster.particles.main.data.database.setter.MySqlSetter;
import net.boster.particles.main.data.database.setter.SQLiteSetter;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.utils.ConfigUtils;
import net.boster.particles.main.utils.CustomTrailsUtils;
import net.boster.particles.main.utils.log.LogType;
import net.boster.particles.main.utils.item.ItemManager;
import net.boster.particles.main.utils.item.ItemManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class BPLoader {

    private final BosterParticles plugin;

    public int RunnableDelay = 0;
    public boolean LoadPlayerTrailsAsync = true;

    @Getter @Setter @NotNull private ItemManager itemManager;

    public final Set<LogType> enabledLoggers = new HashSet<>();

    @Getter @Setter @NotNull private String prefix = "BosterParticles";
    @Getter @NotNull private EnumStorage storage;
    @Getter @Nullable private ConnectedDatabase connectedDatabase;

    public BPLoader(BosterParticles plugin) {
        this.plugin = plugin;
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        this.itemManager = new ItemManagerImpl();

        if(plugin.getConfig().getBoolean("Settings.CheckForUpdates", false)) {
            plugin.enabledUpdater();
        }

        try {
            storage = EnumStorage.valueOf(plugin.getConfig().getString("Storage"));
        } catch (Exception e) {
            storage = EnumStorage.YAML;
        }
    }

    public void load() {
        new Commands(plugin);
        for(String l : plugin.getConfig().getStringList("Settings.EnabledLoggers")) {
            try {
                enabledLoggers.add(LogType.valueOf(l));
            } catch (Exception ignored) {}
        }
        loadFiles();
        loadMenusFiles();
        loadMenus();
        loadDataSetter();
        DataConverter.load();
        CustomTrailsUtils.load();
        plugin.getServer().getScheduler().runTaskLater(plugin, this::loadPlayers, 20);
        BosterCommand.load();
    }

    public void loadFiles() {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("config.yml")));
        File cf = new File(plugin.getDataFolder(), "config.yml");
        if(!ConfigUtils.hasAllStrings(cfg, YamlConfiguration.loadConfiguration(cf))) {
            ConfigUtils.replaceOldConfig(cf, cf, plugin.getResource("config.yml"));
        }
        File usage = new File(plugin.getDataFolder(), "usage_" + plugin.getDescription().getVersion() + ".txt");
        if(!usage.exists()) {
            try {
                Files.copy(plugin.getResource("usage.txt"), usage.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(File f : plugin.getDataFolder().listFiles()) {
            if(f.getName().startsWith("usage") && !f.getName().equalsIgnoreCase("usage_" + plugin.getDescription().getVersion() + ".txt")) {
                f.delete();
            }
        }

        prefix = plugin.getConfig().getString("Settings.Prefix", "BosterParticles");
    }

    public void loadStorage() {
        try {
            storage = EnumStorage.valueOf(plugin.getConfig().getString("Storage"));
        } catch (Exception e) {
            storage = EnumStorage.YAML;
        }
    }

    public void loadMenusFiles() {
        File f = new File(plugin.getDataFolder() + "/menus");
        if(!f.exists() || f.exists() && f.listFiles().length == 0) {
            plugin.saveResource("menus/particles.yml", false);
            plugin.saveResource("menus/player.yml", false);
        }
        File nf = new File(plugin.getDataFolder() + "/menus");
        if(nf.listFiles().length > 0) {
            for(File fl : nf.listFiles()) {
                if(!fl.getName().endsWith(".yml")) continue;

                MenuFile file = new MenuFile(fl);
                file.create();
            }
        }
        LoadPlayerTrailsAsync = plugin.getConfig().getBoolean("Settings.LoadPlayerTrailsAsync", true);
        RunnableDelay = plugin.getConfig().getInt("Settings.RunnableDelay", 0);
    }

    private void loadDataSetter() {
        if(connectedDatabase != null) {
            connectedDatabase.closeConnection();
        }

        loadStorage();

        if(storage == EnumStorage.MYSQL) {
            String host = plugin.getConfig().getString("MySql.host");
            int port = plugin.getConfig().getInt("MySql.port");
            String user = plugin.getConfig().getString("MySql.user");
            String password = plugin.getConfig().getString("MySql.password");
            String db = plugin.getConfig().getString("MySql.database");

            MySqlConnectionUtils con = new MySqlConnectionUtils();
            if ((host != null || user != null || password != null || db != null) && con.connect(host, port, db, user, password)) {
                plugin.setDataSetter(new MySqlSetter(con));
                connectedDatabase = con;
            } else {
                plugin.setDataSetter(new FileSetter());
            }
        } else if(storage == EnumStorage.SQLITE) {
            SQLiteConnectionUtils con = new SQLiteConnectionUtils();
            if(con.connect()) {
                plugin.setDataSetter(new SQLiteSetter(con));
                connectedDatabase = con;
            } else {
                plugin.setDataSetter(new FileSetter());
            }
        } else {
            plugin.setDataSetter(new FileSetter());
        }

        plugin.log("&7Using storage: &9" + plugin.getDataSetter().getName(), LogType.INFO);
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
