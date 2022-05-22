package net.boster.particles.main.api.extension;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface PermissionsExtension extends BPExtension {

    /**
     * @return empty if you want BosterParticles to do a default permission check.
     */
    @NotNull Optional<Boolean> hasPermission(@NotNull Player p, @NotNull String permission);
}
