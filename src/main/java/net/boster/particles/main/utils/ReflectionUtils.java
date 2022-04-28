package net.boster.particles.main.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ReflectionUtils {

    public static void registerCommand(@NotNull Command command) {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            SimpleCommandMap map = (SimpleCommandMap) f.get(Bukkit.getServer());

            map.register("bosterparticles", command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterCommand(@NotNull Command command) {
        try {
            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
            Map<String, Command> map = (Map<String, Command>) knownCommands.get(commandMap.get(Bukkit.getServer()));
            map.remove(command.getName());
            map.remove( "bosterparticles:" + command.getName());
            for(String alias : command.getAliases()) {
                map.remove(alias);
                map.remove("bosterparticles:" + alias);
            }
            command.unregister((CommandMap) commandMap.get(Bukkit.getServer()));
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
