package net.boster.particles.main;

import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.data.database.MySqlConnectionUtils;
import net.boster.particles.main.data.database.setter.FileSetter;
import net.boster.particles.main.data.database.setter.MySQLSetter;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.utils.CustomTrailsUtils;
import net.boster.particles.main.utils.LogType;
import net.boster.particles.main.utils.creator.ItemCreator;
import net.boster.particles.main.utils.creator.NewItemCreator;
import net.boster.particles.main.utils.creator.OldItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
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
        loadMenus();
        loadDataSetter();
        DataConverter.load();
        CustomTrailsUtils.load();
        plugin.getServer().getScheduler().runTaskLater(plugin, this::loadPlayers, 20);
    }

    public void loadFiles() {
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
        if(plugin.getConfig().getBoolean("MySql.Enabled", false)) {
            if(MySqlConnectionUtils.getConnection() != null) {
                try {
                    BosterParticles.getInstance().log("Closing connection to database", LogType.INFO);
                    MySqlConnectionUtils.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

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
