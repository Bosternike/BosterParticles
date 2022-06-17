package net.boster.particles.main.utils;

import lombok.Getter;
import org.bukkit.Bukkit;

public enum Version {

    OLD_VERSION(0),
    v1_8_R1(1),
    v1_8_R2(2),
    v1_8_R3(3),
    v1_9_R1(4),
    v1_9_R2(5),
    v1_10_R1(6),
    v1_11_R1(7),
    v1_12_R1(8),
    v1_13_R2(9),
    v1_14_R1(10),
    v1_15_R1(11),
    v1_16_R1(12),
    v1_16_R2(13),
    v1_16_R3(14),
    v1_17_R1(15),
    v1_18_R1(16),
    v1_18_R2(17),
    v1_19_R1(18),
    v1_19_R2(19),
    NOT_FOUND(777);

    private static Version currentVersion;
    @Getter private final int versionInteger;

    Version(int versionInteger) {
        this.versionInteger = versionInteger;
    }

    public static Version getCurrentVersion() {
        if(currentVersion == null) {
            String ver = Bukkit.getServer().getClass().getPackage().getName();
            String version = ver.substring(ver.lastIndexOf('.') + 1);
            try {
                int cv = Integer.parseInt(version.split("_")[1]);
                if(cv < 8) {
                    return (currentVersion = OLD_VERSION);
                }
            } catch (Exception e) {
                return (currentVersion = OLD_VERSION);
            }

            try {
                currentVersion = valueOf(version);
            } catch (IllegalArgumentException e) {
                currentVersion = NOT_FOUND;
            }
        }
        return currentVersion;
    }
}
