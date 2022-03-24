package net.boster.particles.main.data;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.data.database.DatabaseRunnable;
import net.boster.particles.main.data.database.setter.FileSetter;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.files.UserFile;
import net.boster.particles.main.particle.CraftTrail;
import net.boster.particles.main.utils.LogType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData {

    private static final HashMap<String, Class<? extends PlayerDataExtension>> registeredExtensions = new HashMap<>();
    private static final HashMap<Player, PlayerData> hash = new HashMap<>();

    public final HashMap<String, PlayerDataExtension> extensions = new HashMap<>();
    public final List<CraftTrail> trails = new ArrayList<>();

    public final Player p;

    public FileConfiguration data;

    public PlayerData(Player p) {
        this.p = p;
        hash.put(p, this);
    }

    public static PlayerData load(Player p) {
        PlayerData data = new PlayerData(p);

        if(BosterParticles.getInstance().getLoader().LoadPlayerTrailsAsync) {
            new DatabaseRunnable().run(() -> {
                data.data = BosterParticles.getInstance().getDataSetter().configuration(DataConverter.convert(p));
                data.updateSync();
            });
        } else {
            data.data = BosterParticles.getInstance().getDataSetter().configuration(DataConverter.convert(p));
            data.updateSync();
        }

        return data;
    }

    public void update() {
        trails.clear();
        if(BosterParticles.getInstance().getLoader().LoadPlayerTrailsAsync) {
            new DatabaseRunnable().run(this::updateSync);
        } else {
            updateSync();
        }
    }

    private void updateSync() {
        for(Class<? extends CraftTrail> clazz : CraftTrail.registration) {
            try {
                clazz.getMethod("load", PlayerData.class).invoke(null, this);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                BosterParticles.getInstance().log("&7Could not find static method load(PlayerData.class) in \"&c" + clazz.getName() + "&7\"", LogType.ERROR);
            }
        }
        for(Map.Entry<String, Class<? extends PlayerDataExtension>> e : registeredExtensions.entrySet()) {
            try {
                extensions.put(e.getKey(), e.getValue().getConstructor(String.class, PlayerData.class).newInstance(e.getKey(), this));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                BosterParticles.getInstance().log("&7Could not load extension \"&c" + e.getKey() + "&7\"", LogType.ERROR);
            }
        }
    }

    public static PlayerData get(Player p) {
        if(p == null) return null;

        return hash.get(p);
    }

    public void clear() {
        for(PlayerDataExtension e : extensions.values()) {
            e.onClear();
        }
        if(BosterParticles.getInstance().getDataSetter() instanceof FileSetter) {
            UserFile f = UserFile.get(DataConverter.convert(p));
            if(f != null) {
                f.clear();
            }
        }
        hash.remove(p);
    }

    public static void clearAll() {
        hash.clear();
    }

    public void saveData() {
        for(PlayerDataExtension e : extensions.values()) {
            e.saveData();
        }
        if(data != null) {
            BosterParticles.getInstance().getDataSetter().save(DataConverter.convert(p), data);
        }
    }

    public void clearSection(String name) {
        for(PlayerDataExtension e : extensions.values()) {
            e.onSectionCleared(name);
        }
        data.set(name, null);
    }

    public void clearFile() {
        for(PlayerDataExtension e : extensions.values()) {
            e.onFileCleared();
        }
        for(String k : data.getKeys(false)) {
            data.set(k, null);
        }
    }

    public static boolean registerExtension(@NotNull String id, @NotNull Class<? extends PlayerDataExtension> clazz) {
        if(registeredExtensions.containsKey(id)) {
            BosterParticles.getInstance().log("&7Could not register extension: &c" + id + " &7already exists.", LogType.WARNING);
            return false;
        } else {
            registeredExtensions.put(id, clazz);
            return true;
        }
    }
}
