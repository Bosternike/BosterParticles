package net.Boster.particles.main.utils;

import net.Boster.particles.main.BosterParticles;
import net.Boster.particles.main.data.PlayerData;
import net.Boster.particles.main.data.database.DataConverter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomTrails {

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

        public BosterSound sound;
        public List<String> messages;
        public List<String> commands;
        public List<String> announce;

        public boolean clearFile = false;
        public List<String> clearSections = new ArrayList<>();
        public ConfigurationSection setToFile;

        public static @NotNull CustomTrail load(ConfigurationSection section) {
            if(section == null) {
                return new CustomTrail();
            }

            CustomTrail a = new CustomTrail();
            a.sound = BosterSound.load(section.getString("sound"));
            a.messages = section.getStringList("message");
            a.commands = section.getStringList("commands");
            a.announce = section.getStringList("announce");

            a.setToFile = section.getConfigurationSection("SetToFile");

            a.clearFile = section.getBoolean("ClearFile", false);
            a.clearSections = section.getStringList("ClearSections");
            return a;
        }

        public void act(OfflinePlayer user) {
            Player p = Bukkit.getPlayer(user.getUniqueId());
            if(p != null) {
                if(sound != null) {
                    p.playSound(p.getLocation(), sound.sound, 1, sound.i);
                }
                for(String s : messages) {
                    p.sendMessage(Utils.toColor(s.replace("%player%", p.getName())));
                }
                for(String s : commands) {
                    p.chat(s.replace("%player%", p.getName()));
                }
                for(String s : announce) {
                    Bukkit.broadcastMessage(Utils.toColor(s.replace("%player%", user.getName())));
                }
            }

            PlayerData pd = PlayerData.get(p);
            FileConfiguration file;
            if(pd == null) {
                file = BosterParticles.getInstance().getDataSetter().configuration(user.getName());
            } else {
                file = pd.data;
            }
            boolean ld = false;

            if(clearFile) {
                for(String s : file.getKeys(false)) {
                    file.set(s, null);
                }
                ld = true;
            }
            for(String s : clearSections) {
                file.set(s, null);
                ld = true;
            }
            if(setToFile != null && setToFile.getKeys(false).size() > 0) {
                for(String sc : setToFile.getKeys(false)) {
                    file.set(sc, setToFile.getConfigurationSection(sc));
                }
                ld = true;
            }
            if(ld) {
                if(pd != null) {
                    pd.saveData();
                    pd.update();
                } else {
                    BosterParticles.getInstance().getDataSetter().save(DataConverter.convert(user), file);
                }
            }
        }
    }
}
