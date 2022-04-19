package net.boster.particles.main;

import net.boster.particles.main.commands.BosterCommand;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.data.database.MySqlConnectionUtils;
import net.boster.particles.main.data.database.setter.FileSetter;
import net.boster.particles.main.data.database.setter.MySQLSetter;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.utils.ConfigUtils;
import net.boster.particles.main.utils.CustomTrailsUtils;
import net.boster.particles.main.utils.LogType;
import net.boster.particles.main.utils.creator.ItemCreator;
import net.boster.particles.main.utils.creator.NewItemCreator;
import net.boster.particles.main.utils.creator.OldItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.SQLException;

public class BPLoader {

    private final BosterParticles plugin;

    public int RunnableDelay = 0;
    public boolean LoadPlayerTrailsAsync = true;

    private final boolean isOldVersion;
    private final ItemCreator itemCreator;

    public BPLoader(BosterParticles plugin) {
        this.plugin = plugin;
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        this.isOldVersion = Integer.parseInt(ver.substring(ver.lastIndexOf('.') + 1).split("_")[1]) < 13;
        if(this.isOldVersion) {
            this.itemCreator = new OldItemCreator();
        } else {
            this.itemCreator = new NewItemCreator();
        }

        if(plugin.getConfig().getBoolean("Settings.CheckForUpdates", false)) {
            plugin.enabledUpdater();
        }
    }

    public void load() {
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
        if(MySqlConnectionUtils.getConnection() != null) {
            try {
                MySqlConnectionUtils.stopAntiTimeOut();
                MySqlConnectionUtils.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(plugin.getConfig().getBoolean("MySql.Enabled", false)) {
            String host = BosterParticles.getInstance().getConfig().getString("MySql.host");
            int port = BosterParticles.getInstance().getConfig().getInt("MySql.port");
            String user = BosterParticles.getInstance().getConfig().getString("MySql.user");
            String password = BosterParticles.getInstance().getConfig().getString("MySql.password");
            String db = BosterParticles.getInstance().getConfig().getString("MySql.database");

            if((host != null || user != null || password != null || db != null) && MySqlConnectionUtils.connect(host, port, db, user, password)) {
                plugin.setDataSetter(new MySQLSetter());
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
        load();
    }

    public boolean isOldVersion() {
        return isOldVersion;
    }

    public ItemCreator getItemCreator() {
        return itemCreator;
    }
}
