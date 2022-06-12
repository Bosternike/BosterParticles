package net.boster.particles.main.lib;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PAPISupport {

    public static void load() {
        PlaceholderAPISupport.load();
    }

    public static String setPlaceholders(OfflinePlayer p, String s) {
        if (PlaceholderAPISupport.isLoaded()) {
            return PlaceholderAPISupport.setPlaceholders(p, s);
        } else {
            return s;
        }
    }
}
