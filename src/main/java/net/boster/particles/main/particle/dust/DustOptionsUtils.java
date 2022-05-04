package net.boster.particles.main.particle.dust;

import net.boster.particles.main.utils.Version;
import org.jetbrains.annotations.NotNull;

public class DustOptionsUtils {

    private static final DustOptionsProvider provider;

    static {
        if(Version.getCurrentVersion().getVersionInteger() < 9) {
            provider = new OldDustOptionsProvider();
        } else {
            provider = new NewDustOptionsProvider();
        }
    }

    public static @NotNull Object create(int r, int g, int b, int amount) {
        return provider.create(r, g, b, amount);
    }
}
