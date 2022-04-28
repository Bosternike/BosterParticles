package net.boster.particles.main.utils.particle.blockdata;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NewBlockDataProvider implements BlockDataProvider {

    @Override
    public @Nullable Object create(@NotNull Material material) {
        return material.createBlockData();
    }
}
