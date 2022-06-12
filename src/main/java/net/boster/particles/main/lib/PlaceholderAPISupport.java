package net.boster.particles.main.lib;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPISupport {

    private static boolean isLoaded = false;

    public static void load() {
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            isLoaded = true;
        } catch (NoClassDefFoundError | Exception ignored) {}
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static String setPlaceholders(OfflinePlayer p, String s) {
        return PlaceholderAPI.setPlaceholders(p, s);
    }
}
