package net.Boster.particles.main.data.database.setter;

import org.bukkit.configuration.file.FileConfiguration;

public interface DataSetter {

    void setUserData(String uuid, String w, String o);

    String getUserData(String uuid, String value);

    FileConfiguration configuration(String uuid);

    void save(String uuid, FileConfiguration file);

    void deleteUser(String uuid);
}
