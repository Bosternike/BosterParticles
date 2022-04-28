package net.boster.particles.main.files;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class CustomFile {

    @Getter @Setter @NotNull private String fileName;
    @Getter private FileConfiguration configuration;
    @Getter private File file;
    @Getter @Setter @Nullable private String dir = "";
    @Getter @Setter @NotNull private String expansion = "yml";

    public CustomFile(@NotNull String fileName) {
        this.fileName = fileName;
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

    public void createFromSource(String source) {
        file = new File(BosterParticles.getInstance().getDataFolder(), getFileWithDirectory());
        if(!file.exists()) {
            try {
                Files.copy(Objects.requireNonNull(BosterParticles.getInstance().getResource(source)), file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void create() {
        createFromSource(getFileWithDirectory());
    }

    public void createNewFile() {
        file = new File(BosterParticles.getInstance().getDataFolder(), getFileWithDirectory());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public String getFileWithDirectory() {
        return (dir != null ? dir : "") + fileName + "." + expansion;
    }
}
