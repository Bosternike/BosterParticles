package net.boster.particles.main.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {

    public static final HashMap<String, Class<?>> classCache = new HashMap<>();
    public static final String version;
    public static final int versionInt;

    private static Method playerHandle;
    private static Field playerConnection;
    private static Method sendPacket;

    private static SimpleCommandMap commandMap;

    private static Method oldPlayerLocaleMethod;

    static {
        version = Version.getCurrentVersion().name();
        versionInt = Integer.parseInt(version.split("_")[1]);

        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (SimpleCommandMap) f.get(Bukkit.getServer());

            if(versionInt < 4) {
                classCache.put("EnumParticle", Class.forName("net.minecraft.server." + version + ".EnumParticle"));
                classCache.put("PacketPlayOutWorldParticles", Class.forName("net.minecraft.server." + version + ".PacketPlayOutWorldParticles"));
                classCache.put("Packet", Class.forName("net.minecraft.server." + version + ".Packet"));
                classCache.put("EntityPlayer", Class.forName("net.minecraft.server." + version + ".EntityPlayer"));
                classCache.put("PlayerConnection", Class.forName("net.minecraft.server." + version + ".PlayerConnection"));

                classCache.put("CraftServer", Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer"));
                classCache.put("CraftPlayer", Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer"));

                playerHandle = classCache.get("CraftPlayer").getMethod("getHandle");
                playerConnection = classCache.get("EntityPlayer").getField("playerConnection");
                sendPacket = classCache.get("PlayerConnection").getMethod("sendPacket", classCache.get("Packet"));
            }
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }

        try {
            oldPlayerLocaleMethod = Player.Spigot.class.getMethod("getLocale");
        } catch (Throwable ignored) {}
    }

    public static Object getEnumParticle(@NotNull String s) {
        try {
            return classCache.get("EnumParticle").getMethod("valueOf", String.class).invoke(null, s);
        } catch (Exception e) {
            return null;
        }
    }

    public static void sendParticle(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options, int range, float speed) {
        try {
            Object packet = classCache.get("PacketPlayOutWorldParticles").getDeclaredConstructor(particle.getClass(), boolean.class, float.class, float.class, float.class,
                    float.class, float.class, float.class, float.class, int.class, int[].class).newInstance(particle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) x, (float) y,
                    (float) z, speed, amount, new int[]{(int) options});
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getWorld() == loc.getWorld() && p.getLocation().distance(loc) <= range) {
                    sendPacket(p, packet);
                }
            }
        } catch (Exception ignored) {}
    }

    public static void sendParticle(@NotNull Object particle, @NotNull Location loc, int amount, double x, double y, double z, double options, int range) {
        sendParticle(particle, loc, amount, x, y, z, options, range, 0);
    }

    public static void sendPacket(@NotNull Player p, @NotNull Object packet) {
        try {
            Object np = playerHandle.invoke(p);
            Object c = playerConnection.get(np);
            sendPacket.invoke(c, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerCommand(@NotNull Command command) {
        try {
            commandMap.register("bosterparticles", command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregisterCommand(@NotNull Command command) {
        try {
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            knownCommands.setAccessible(true);
            Map<String, Command> map = (Map<String, Command>) knownCommands.get(commandMap);
            map.remove(command.getName());
            map.remove( "bosterparticles:" + command.getName());
            for(String alias : command.getAliases()) {
                map.remove(alias);
                map.remove("bosterparticles:" + alias);
            }
            command.unregister(commandMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void syncCommands() {
        try {
            if(Version.getCurrentVersion().getVersionInteger() >= 12) {
                Method m = Bukkit.getServer().getClass().getMethod("syncCommands");
                m.setAccessible(true);
                m.invoke(Bukkit.getServer());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLocale(@NotNull Player p) {
        if(oldPlayerLocaleMethod != null) {
            try {
                return (String) oldPlayerLocaleMethod.invoke(p.spigot());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return p.getLocale();
    }
}
