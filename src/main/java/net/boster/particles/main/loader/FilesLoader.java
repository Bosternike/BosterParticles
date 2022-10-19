package net.boster.particles.main.loader;

import com.google.common.collect.Lists;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.utils.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;

public class FilesLoader extends ALoader {

    public FilesLoader(@NotNull BPLoader loader, @NotNull BosterParticles plugin) {
        super(loader, plugin);
    }

    public void loadFiles() {
        processConfiguration("config.yml", Lists.newArrayList("CustomTrails"));
        copyFile("usage.txt", "usage_" + plugin.getDescription().getVersion() + ".txt");

        for(File f : plugin.getDataFolder().listFiles()) {
            if(f.getName().startsWith("usage") && !f.getName().equalsIgnoreCase("usage_" + plugin.getDescription().getVersion() + ".txt")) {
                f.delete();
            }
        }

        loader.setPrefix(plugin.getConfig().getString("Settings.Prefix", "BosterParticles"));
    }

    private void processConfiguration(@NotNull String name, @NotNull List<String> skippedFields) {
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(name)));
        File cf = new File(plugin.getDataFolder(), name);
        if(!ConfigUtils.hasAllStrings(cfg, YamlConfiguration.loadConfiguration(cf), skippedFields)) {
            ConfigUtils.replaceOldConfig(cf, cf, plugin.getResource(name));
            plugin.reloadConfig();
        }
    }

    private void copyFile(@NotNull String name, @NotNull String toName) {
        File file = new File(plugin.getDataFolder(), toName);
        if(!file.exists()) {
            try {
                Files.copy(plugin.getResource(name), file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
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

        loader.LoadPlayerTrailsAsync = plugin.getConfig().getBoolean("Settings.LoadPlayerTrailsAsync", true);
        loader.RunnableDelay = plugin.getConfig().getInt("Settings.RunnableDelay", 0);
    }
}
