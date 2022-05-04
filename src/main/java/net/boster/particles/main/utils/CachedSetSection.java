package net.boster.particles.main.utils;

import lombok.AllArgsConstructor;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.log.LogType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CachedSetSection {

    public boolean clearFile;
    public @NotNull List<String> clearSections;
    public @Nullable ConfigurationSection setToFile;

    public CachedSetSection() {
        this(false, new ArrayList<>(), null);
    }

    public static CachedSetSection load(@NotNull ConfigurationSection section, @NotNull String errorMessage) {
        CachedSetSection set;
        if(section.getString("fromBytes") != null) {
            set = new CachedSetSection();
            FileConfiguration c = new YamlConfiguration();
            try {
                c.loadFromString(Utils.decode(section.getString("fromBytes")));
                set.clearFile = c.getBoolean("ClearFile", false);
                set.clearSections = c.getStringList("ClearSections");
                set.setToFile = c.getConfigurationSection("SetToFile");
            } catch (InvalidConfigurationException e) {
                BosterParticles.getInstance().log(errorMessage, LogType.ERROR);
            }
        } else {
            set = new CachedSetSection(section.getBoolean("ClearFile", false), section.getStringList("ClearSections"),
                    section.getConfigurationSection("SetToFile"));
        }

        return set;
    }

    public static CachedSetSection load(@NotNull ConfigurationSection section) {
        return load(section, "Could not load from bytes from path " + section.getCurrentPath() + "!");
    }
}
