package net.boster.particles.main.data;

import lombok.Getter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.api.extension.EconomyExtension;
import net.boster.particles.api.extension.PermissionsExtension;
import net.boster.particles.api.extension.PlaceholdersExtension;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.data.database.DatabaseRunnable;
import net.boster.particles.main.data.database.setter.FileSetter;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.files.UserFile;
import net.boster.particles.main.lib.VaultSupport;
import net.boster.particles.main.trail.CraftTrail;
import net.boster.particles.main.utils.ReflectionUtils;
import net.boster.particles.main.utils.log.LogType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PlayerData {

    private static final HashMap<String, Class<? extends PlayerDataExtension>> registeredExtensions = new HashMap<>();
    private static final HashMap<Player, PlayerData> hash = new HashMap<>();

    public final HashMap<String, PlayerDataExtension> extensions = new HashMap<>();
    public final List<CraftTrail> trails = new ArrayList<>();

    @Getter @NotNull private final Player player;

    @NotNull public EConfiguration data = new EConfiguration();

    @Nullable public EconomyExtension economyExtension;
    @Nullable public PermissionsExtension permissionsExtension;
    @Nullable public PlaceholdersExtension placeholdersExtension;

    public PlayerData(@NotNull Player p) {
        this.player = p;
        hash.put(p, this);
    }

    public static PlayerData load(@NotNull Player p) {
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
        for(PlayerDataExtension e : extensions.values()) {
            e.onUpdate();
        }
        if(BosterParticles.getInstance().getLoader().LoadPlayerTrailsAsync) {
            new DatabaseRunnable().run(this::updateSync);
        } else {
            updateSync();
        }
    }

    private void updateSync() {
        for(Map.Entry<String, Class<? extends PlayerDataExtension>> e : registeredExtensions.entrySet()) {
            try {
                extensions.put(e.getKey(), e.getValue().getConstructor(String.class, PlayerData.class).newInstance(e.getKey(), this));
            } catch (Throwable ex) {
                BosterParticles.getInstance().log("&7Could not load extension \"&c" + e.getKey() + "&7\"", LogType.ERROR);
                ex.printStackTrace();
            }
        }
        for(Class<? extends CraftTrail> clazz : CraftTrail.registration) {
            try {
                clazz.getMethod("load", PlayerData.class).invoke(null, this);
            } catch (Throwable e) {
                BosterParticles.getInstance().log("&7Could not find static method load(PlayerData.class) in \"&c" + clazz.getName() + "&7\"", LogType.ERROR);
                e.printStackTrace();
            }
        }
    }

    public static PlayerData get(@NotNull Player p) {
        return hash.get(p);
    }

    public void clear() {
        for(PlayerDataExtension e : extensions.values()) {
            e.onClear();
        }
        if(BosterParticles.getInstance().getDataSetter() instanceof FileSetter) {
            UserFile f = UserFile.get(DataConverter.convert(player));
            if(f != null) {
                f.clear();
            }
        }
        hash.remove(player);
    }

    public static void clearAll() {
        hash.clear();
    }

    public void saveData() {
        for(PlayerDataExtension e : extensions.values()) {
            e.saveData();
        }

        if(!data.isEdited()) return;

        BosterParticles.getInstance().getDataSetter().save(DataConverter.convert(player), data);
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

    public static Collection<Class<? extends PlayerDataExtension>> getRegisteredExtensions() {
        return registeredExtensions.values();
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

    public double requestBalance() {
        double originalAmount = VaultSupport.getBalance(player);

        if(economyExtension != null) {
            try {
                Optional<Double> o = economyExtension.requestBalance(player, originalAmount);
                return o.orElse(originalAmount);
            } catch (Throwable e) {
                BosterParticles.getInstance().log("Generated an exception:", "EconomyExtension", economyExtension, LogType.ERROR);
                e.printStackTrace();
                return originalAmount;
            }
        } else {
            return originalAmount;
        }
    }

    public void withdrawMoney(double amount) {
        if(economyExtension != null) {
            try {
                Optional<Double> o = economyExtension.withdrawMoney(player, amount);
                if(o == null) {
                    return;
                } else {
                    if(o.isPresent()) {
                        VaultSupport.withdrawMoney(player, o.get());
                        return;
                    }
                }
            } catch (Throwable e) {
                BosterParticles.getInstance().log("Generated an exception:", "EconomyExtension", economyExtension, LogType.ERROR);
                e.printStackTrace();
            }
        }

        VaultSupport.withdrawMoney(player, amount);
    }

    public boolean hasPermission(@NotNull String permission) {
        if(permissionsExtension != null) {
            try {
                Optional<Boolean> o = permissionsExtension.hasPermission(player, permission);
                if(o.isPresent()) {
                    return o.get();
                }
            } catch (Throwable e) {
                BosterParticles.getInstance().log("Generated an exception:", "PermissionsExtension", permissionsExtension, LogType.ERROR);
                e.printStackTrace();
            }
        }

        return player.hasPermission(permission);
    }

    public @NotNull String setPlaceholders(@NotNull String s) {
        if(placeholdersExtension != null) {
            try {
                return placeholdersExtension.setPlaceholders(player, s);
            } catch (Throwable e) {
                BosterParticles.getInstance().log("Generated an exception:", "PlaceholdersExtension", placeholdersExtension, LogType.ERROR);
                e.printStackTrace();
            }
        }

        return s;
    }

    public @NotNull String getLocale() {
        return ReflectionUtils.getLocale(player).toLowerCase();
    }
}
