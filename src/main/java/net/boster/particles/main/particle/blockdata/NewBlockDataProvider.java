package net.boster.particles.main.particle.blockdata;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NewBlockDataProvider implements BlockDataProvider {

    @Override
    public @Nullable BPBlockData create(@NotNull Material material) {
        return material::createBlockData;
    }
}
