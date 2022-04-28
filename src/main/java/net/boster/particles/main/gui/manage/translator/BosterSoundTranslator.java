package net.boster.particles.main.gui.manage.translator;

import net.boster.particles.main.utils.BosterSound;
import net.boster.particles.main.utils.Utils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BosterSoundTranslator implements ITranslator {

    @Override
    public Optional<String> translate(@NotNull Player p, @NotNull String input) {
        try {
            return Optional.of(BosterSound.load(input).toString());
        } catch (Exception e) {
            p.sendMessage(Utils.toColor("%prefix% &c" + input + "&f is not a valid sound."));
            return Optional.empty();
        }
    }
}
