package net.boster.particles.main.gui.manage.translator;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ITranslator {

    <T> Optional<T> translate(@NotNull Player p, @NotNull String input);
}
