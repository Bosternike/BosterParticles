package net.boster.particles.main.files;

import net.boster.particles.main.BosterParticles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UserFile {

    private static final HashMap<String, UserFile> hash = new HashMap<>();

    private FileConfiguration configuration;
    private File file;
    private final String name;

    public UserFile(String name) {
        hash.put(name, this);
        this.name = name;
    }

    public static UserFile get(String file) {
        return hash.get(file);
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create() {
        file = new File(BosterParticles.getInstance().getDataFolder() + "/users", name + ".yml");
        if(!file.exists()) {
            File fd = new File(BosterParticles.getInstance().getDataFolder() + "/users");
            if(!fd.exists()) {
                fd.mkdir();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public String getName() {
        return name;
    }

    public void clear() {
        hash.remove(name);
    }

    public static void clearAll() {
        hash.clear();
    }
}
