package net.boster.particles.main.particle.blockdata;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BlockDataProvider {

    @Nullable BPBlockData create(@NotNull Material material);
}
