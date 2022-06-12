package net.boster.particles.api.extension;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlaceholdersExtension extends BPExtension {

    @NotNull String setPlaceholders(@NotNull Player p, @NotNull String s);
}
