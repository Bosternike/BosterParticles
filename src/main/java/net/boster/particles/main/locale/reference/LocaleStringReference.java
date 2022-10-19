package net.boster.particles.main.locale.reference;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.reference.StringReference;
import org.jetbrains.annotations.NotNull;

public class LocaleStringReference extends LocaleReference<String> implements StringReference<String> {

    public LocaleStringReference(@NotNull String path) {
        super(fileConfiguration -> fileConfiguration.getString(BosterParticles.getInstance().getLocalesManager().toPath(path)));
    }
}
