package net.boster.particles.main.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionUtils {

    public static void registerCommand(@NotNull String s, @NotNull Command command) {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            SimpleCommandMap map = (SimpleCommandMap) f.get(Bukkit.getServer());

            map.register(s.toLowerCase(), command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterCommand(@NotNull Command command) {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            SimpleCommandMap map = (SimpleCommandMap) f.get(Bukkit.getServer());

            Field mf = SimpleCommandMap.class.getDeclaredField("knownCommands");
            mf.setAccessible(true);
            HashMap<String, Command> cmds = (HashMap<String, Command>) mf.get(map);
            cmds.remove(command.getName().toLowerCase());
            for(String alias : command.getAliases()) {
                cmds.remove(alias.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncCommands() {
        try {
            Method m = Bukkit.getServer().getClass().getMethod("syncCommands");
            m.setAccessible(true);
            m.invoke(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
