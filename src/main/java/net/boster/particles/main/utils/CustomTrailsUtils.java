package net.boster.particles.main.utils;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.DataConverter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomTrailsUtils {

    private static final HashMap<String, CustomTrail> hash = new HashMap<>();

    public static void load() {
        hash.clear();

        ConfigurationSection section = BosterParticles.getInstance().getConfig().getConfigurationSection("CustomTrails");
        if(section != null) {
            for(String k : section.getKeys(false)) {
                hash.put(k, CustomTrail.load(section.getConfigurationSection(k)));
            }
        }
    }

    public static CustomTrail get(String s) {
        return hash.get(s);
    }

    public static class CustomTrail {

        public final BosterSound sound;
        public final List<String> messages;
        public final List<String> commands;
        public final List<String> announce;

        public final CachedSetSection set;

        public CustomTrail(@Nullable BosterSound sound, @NotNull List<String> messages, @NotNull List<String> commands, @NotNull List<String> announce, @NotNull CachedSetSection set) {
            this.sound = sound;
            this.messages = messages;
            this.commands = commands;
            this.announce = announce;
            this.set = set;
        }

        public static @NotNull CustomTrail load(ConfigurationSection section) {
            if(section == null) {
                return new CustomTrail(null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new CachedSetSection());
            }

            return new CustomTrail(BosterSound.load(section.getString("sound")), section.getStringList("message"),
                    section.getStringList("commands"), section.getStringList("announce"), CachedSetSection.load(section));
        }

        public void act(OfflinePlayer user) {
            Player p = Bukkit.getPlayer(user.getUniqueId());
            if(p != null) {
                if(sound != null) {
                    p.playSound(p.getLocation(), sound.sound, 1, sound.i);
                }
                messages.forEach(s -> p.sendMessage(Utils.toColor(s.replace("%player%", p.getName()))));
                commands.forEach(s -> p.chat(s.replace("%player%", p.getName())));
                announce.forEach(s -> Bukkit.broadcastMessage(Utils.toColor(s.replace("%player%", p.getName()))));
            }

            PlayerData pd = PlayerData.get(p);
            FileConfiguration file;
            if(pd == null) {
                file = BosterParticles.getInstance().getDataSetter().configuration(DataConverter.convert(user));
            } else {
                file = pd.data;
            }

            if(set.clearFile) {
                file.getKeys(false).forEach(s -> file.set(s, null));
            }
            if(!set.clearSections.isEmpty()) {
                set.clearSections.forEach(s -> file.set(s, null));
            }
            if(set.setToFile != null && set.setToFile.getKeys(false).size() > 0) {
                set.setToFile.getKeys(false).forEach(sc -> file.set(sc, set.setToFile.getConfigurationSection(sc)));
            }
            if(pd != null) {
                pd.saveData();
                pd.update();
            } else {
                BosterParticles.getInstance().getDataSetter().save(DataConverter.convert(user), file);
            }
        }
    }
}
