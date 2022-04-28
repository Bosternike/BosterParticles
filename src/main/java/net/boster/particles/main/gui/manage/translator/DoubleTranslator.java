package net.boster.particles.main.gui.manage.translator;

import net.boster.particles.main.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DoubleTranslator implements ITranslator {

    @Override
    public Optional<Double> translate(@NotNull Player p, @NotNull String input) {
        try {
            return Optional.of(Double.parseDouble(input));
        } catch (Exception e) {
            p.sendMessage(Utils.toColor("%prefix% &c" + input + "&f is not an double."));
            return Optional.empty();
        }
    }
}
