package net.boster.particles.main.data.database.setter;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.EConfiguration;
import net.boster.particles.main.files.UserFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileSetter implements DataSetter {

    @Override
    public @NotNull String getName() {
        return "YAML";
    }

    @Override
    public void setUserData(@NotNull String uuid, @NotNull String w, String o) {
        BosterParticles.getInstance().getFileManager().getUserFile(uuid).getConfiguration().set(w, o);
        BosterParticles.getInstance().getFileManager().saveUserFile(uuid);
    }

    @Override
    public String getUserData(@NotNull String uuid, @NotNull String value) {
        return BosterParticles.getInstance().getFileManager().getUserFile(uuid).getConfiguration().getString(value);
    }

    @Override
    public @NotNull EConfiguration configuration(@NotNull String uuid) {
        return BosterParticles.getInstance().getFileManager().getUserConfig(uuid);
    }

    @Override
    public void save(@NotNull String uuid, @NotNull EConfiguration file) {
        UserFile f = BosterParticles.getInstance().getFileManager().getUserFile(uuid);
        f.setConfiguration(file);
        f.save();
    }

    @Override
    public void deleteUser(@NotNull String uuid) {
        File f = BosterParticles.getInstance().getFileManager().getUserFile(uuid).getFile();
        if(f.exists()) {
            f.delete();
        }
    }
}
