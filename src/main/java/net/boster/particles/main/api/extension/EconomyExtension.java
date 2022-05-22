package net.boster.particles.main.api.extension;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface EconomyExtension extends BPExtension {

    /**
     * @return empty if you don't want to edit original player balance response.
     */
    @NotNull Optional<Double> requestBalance(@NotNull Player p, double originalAmount);

    /**
     * @return null if BosterParticles shouldn't withdraw money.
     */
    @Nullable Optional<Double> withdrawMoney(@NotNull Player p, double amount);
}
