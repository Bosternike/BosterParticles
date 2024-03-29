package net.boster.particles.main.data;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.files.UserFile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileManager {

    private final BosterParticles plugin;

    public FileManager(BosterParticles plugin) {
        this.plugin = plugin;
    }

    public @NotNull UserFile getUserFile(String name) {
        UserFile file = UserFile.get(name);
        if(file == null) {
            file = new UserFile(name);
        }
        return file;
    }

    public boolean exists(String name) {
        return new File(plugin.getDataFolder() + "/users", name + ".yml").exists();
    }

    public void loadFile(Player p) {
        loadFile(p.getName());
    }

    public void loadFile(String name) {
        getUserFile(name).create();
    }

    public @NotNull EConfiguration getUserConfig(Player p) {
        return getUserConfig(p.getName());
    }

    public @NotNull EConfiguration getUserConfig(String name) {
        UserFile file = UserFile.get(name);
        if(file == null) {
            file = new UserFile(name);
            file.create();
        }
        return file.getConfiguration();
    }

    public void saveUserFile(String name) {
        getUserFile(name).save();
    }
}
