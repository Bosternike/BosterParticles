package net.boster.particles.main.data.database;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.LogType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DataConverter {

    private static DataConverterType dataConverterType;

    public static void load() {
        try {
            dataConverterType = DataConverterType.valueOf(BosterParticles.getInstance().getConfig().getString("Settings.userKeyType"));
        } catch (Exception e) {
            dataConverterType = DataConverterType.NAME;
            BosterParticles.getInstance().log("Could not load DataConverter from config. Using default key type: &6&lNAME", LogType.WARNING);
        }
    }

    public static String convert(Player p) {
        return dataConverterType.convert(p);
    }

    public static String convert(OfflinePlayer p) {
        return dataConverterType.convert(p);
    }

    public static DataConverterType getDataConverterType() {
        return dataConverterType;
    }

    public void setDataConverterType(DataConverterType type) {
        dataConverterType = type;
    }

    public enum DataConverterType {
        NAME,
        UUID {
            @Override
            public String convert(Player p) {
                return p.getUniqueId().toString();
            }

            @Override
            public String convert(OfflinePlayer p) {
                return p.getUniqueId().toString();
            }
        };

        public String convert(Player p) {
            return p.getName();
        }

        public String convert(OfflinePlayer p) {
            return p.getName();
        }
    }
}
