package net.boster.particles.main.files;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.EConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Getter
@Setter
public class UserFile {

    private static final HashMap<String, UserFile> hash = new HashMap<>();

    @NotNull private EConfiguration configuration = new EConfiguration();
    @NotNull private File file;
    @NotNull private final String name;

    public UserFile(@NotNull String name) {
        hash.put(name, this);
        this.name = name;
        this.file = new File(BosterParticles.getInstance().getDataFolder() + "/users", name + ".yml");
    }

    public static UserFile get(String file) {
        return hash.get(file);
    }

    public void reload() {
        configuration = new EConfiguration();
        try {
            configuration.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        reload();
    }

    public void clear() {
        hash.remove(name);
    }

    public static void clearAll() {
        hash.clear();
    }
}
