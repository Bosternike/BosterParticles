package net.boster.particles.main.particle.blockdata;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OldBlockDataProvider implements BlockDataProvider {

    @Override
    public @Nullable BPBlockData create(@NotNull Material material) {
        return () -> new int[]{material.getId(), new ItemStack(material).getData().getData()};
    }
}
