package net.boster.particles.main.data.database.setter;

import net.boster.particles.main.BosterParticles;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileSetter implements DataSetter {

    @Override
    public @NotNull String getName() {
        return "File";
    }

    @Override
    public void setUserData(String uuid, String w, String o) {
        BosterParticles.getInstance().getFileManager().getUserFile(uuid).getConfiguration().set(w, o);
        BosterParticles.getInstance().getFileManager().saveUserFile(uuid);
    }

    @Override
    public String getUserData(String uuid, String value) {
        return BosterParticles.getInstance().getFileManager().getUserFile(uuid).getConfiguration().getString(value);
    }

    @Override
    public FileConfiguration configuration(String uuid) {
        return BosterParticles.getInstance().getFileManager().getUserConfig(uuid);
    }

    @Override
    public void save(String uuid, FileConfiguration file) {
        BosterParticles.getInstance().getFileManager().getUserFile(uuid).save();
    }

    @Override
    public void deleteUser(String uuid) {
        File f = BosterParticles.getInstance().getFileManager().getUserFile(uuid).getFile();
        if(f != null && f.exists()) {
            f.delete();
        }
    }
}
