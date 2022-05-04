package net.boster.particles.main.data.database.setter;

import lombok.RequiredArgsConstructor;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.database.ConnectedDatabase;
import net.boster.particles.main.utils.log.LogType;
import net.boster.particles.main.utils.Utils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

@RequiredArgsConstructor
public abstract class DatabaseSetter implements DataSetter {

    private final ConnectedDatabase db;

    @Override
    public void setUserData(String uuid, String w, String o) {
        db.setMySqlUserValue(uuid, w, o);
    }

    @Override
    public String getUserData(String uuid, String value) {
        return db.getMySqlValue(uuid, value);
    }

    @Override
    public FileConfiguration configuration(String uuid) {
        FileConfiguration c = new YamlConfiguration();
        String data = getUserData(uuid, "data");
        if(data != null) {
            try {
                c.loadFromString(Utils.decode(data));
            } catch (InvalidConfigurationException e) {
                BosterParticles.getInstance().log("Could not load " + uuid + "'s configuration!", LogType.ERROR);
            }
        }
        return c;
    }

    @Override
    public void save(String uuid, FileConfiguration file) {
        setUserData(uuid, "data", Utils.encode(file.saveToString()));
    }

    @Override
    public void deleteUser(String uuid) {
        db.deleteUser(uuid);
    }
}
