package net.boster.particles.main.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigValues {

    public static String DEFAULT_LOCALE;

    public static List<CustomTrailsUtils.CustomTrail> DEFAULT_TRAILS = new ArrayList<>();

    public static void load(@NotNull FileConfiguration c) {
        DEFAULT_LOCALE = c.getString("Settings.DefaultLocale");

        for(String s : c.getStringList("Settings.Defaults")) {
            CustomTrailsUtils.CustomTrail t = CustomTrailsUtils.get(s);
            if(t != null) {
                DEFAULT_TRAILS.add(t);
            }
        }
    }
}
