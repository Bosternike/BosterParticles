package net.boster.particles.main.data.database.setter;

import lombok.RequiredArgsConstructor;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.EConfiguration;
import net.boster.particles.main.data.database.ConnectedDatabase;
import net.boster.particles.main.utils.log.LogType;
import net.boster.particles.main.utils.Utils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class DatabaseSetter implements DataSetter {

    private final ConnectedDatabase db;

    @Override
    public void setUserData(@NotNull String uuid, @NotNull String w, String o) {
        db.setMySqlUserValue(uuid, w, o);
    }

    @Override
    public String getUserData(@NotNull String uuid, @NotNull String value) {
        return db.getMySqlValue(uuid, value);
    }

    @Override
    public @NotNull EConfiguration configuration(@NotNull String uuid) {
        EConfiguration c = new EConfiguration();
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
    public void save(@NotNull String uuid, @NotNull EConfiguration file) {
        setUserData(uuid, "data", Utils.encode(file.saveToString()));
    }

    @Override
    public void deleteUser(@NotNull String uuid) {
        db.deleteUser(uuid);
    }
}
