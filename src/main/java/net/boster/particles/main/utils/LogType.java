package net.boster.particles.main.utils;

import lombok.Getter;

public enum LogType {

    NONE("§d[§bBosterParticles§d] ", "§7", false),
    UPDATER("§d[§bBosterParticles§d] §7[§6UPDATER§7] ", "§7", false),
    FINE("§d[§bBosterParticles§d] §7[§aFINE§7] ", "§a"),
    INFO("§d[§bBosterParticles§d] §7[§9INFO§7] ", "§9"),
    WARNING("§d[§bBosterParticles§d] §7[§cWARNING§7] ", "§c"),
    ERROR("§d[§bBosterParticles§d] §7[§4ERROR§7] ", "§4");

    @Getter private final String format;
    @Getter private final String color;
    @Getter private final boolean toggleAble;

    LogType(String s, String color, boolean toggleAble) {
        this.format = s;
        this.color = color;
        this.toggleAble = toggleAble;
    }

    LogType(String s, String color) {
        this(s, color, true);
    }
}
