package net.boster.particles.main.files;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UserFile {

    private static final HashMap<String, UserFile> hash = new HashMap<>();

    @Getter @Setter @NotNull private FileConfiguration configuration = new YamlConfiguration();
    @Getter @Setter @NotNull private File file;
    @Getter @NotNull private final String name;

    public UserFile(@NotNull String name) {
        hash.put(name, this);
        this.name = name;
        this.file = new File(BosterParticles.getInstance().getDataFolder() + "/users", name + ".yml");
    }

    public static UserFile get(String file) {
        return hash.get(file);
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

    public void clear() {
        hash.remove(name);
    }

    public static void clearAll() {
        hash.clear();
    }
}
