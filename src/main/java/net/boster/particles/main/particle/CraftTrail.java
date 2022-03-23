package net.boster.particles.main.particle;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.LogType;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CraftTrail {

    public static final List<Class<? extends CraftTrail>> registration = new ArrayList<>();

    static {
        registration.add(CraftParticle.class);
        registration.add(CraftItemTrail.class);
    }

    public abstract void spawn(Location loc);

    public static void register(@NotNull Class<? extends CraftTrail> clazz) {
        if(registration.contains(clazz)) {
            BosterParticles.getInstance().log("Could not register Trail class \"&c" + clazz.getName() + "&7\", because it's already registered", LogType.WARNING);
        } else {
            registration.add(clazz);
        }
    }
}
