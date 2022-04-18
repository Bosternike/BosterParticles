package net.boster.particles.main.files;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class MenuFile {

    private static final HashMap<String, MenuFile> hash = new HashMap<>();

    @Getter private FileConfiguration config;
    @Getter private final File file;
    private final String name;

    public MenuFile(File file) {
        String[] ss = file.getName().replace(".", ",").split(",");
        this.name = ss[ss.length - 2];
        hash.put(name, this);
        this.file = file;
    }

    public static MenuFile get(String file) {
        return hash.get(file);
    }

    public void reloadFile() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void create() {
        if(file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        }
    }

    public String getName() {
        return name;
    }

    public void clear() {
        hash.remove(name);
    }

    public static Collection<MenuFile> files() {
        return hash.values();
    }

    public static void clearAll() {
        hash.clear();
    }
}
