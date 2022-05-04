package net.boster.particles.main.particle.blockdata;

import net.boster.particles.main.utils.Version;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockDataUtils {

    private static final BlockDataProvider provider;

    static {
        if(Version.getCurrentVersion().getVersionInteger() < 9) {
            provider = new OldBlockDataProvider();
        } else {
            provider = new NewBlockDataProvider();
        }
    }

    public static @Nullable Object createBlockData(@NotNull Material material) {
        return provider.create(material);
    }
}
