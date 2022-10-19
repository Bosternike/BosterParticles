package net.boster.particles.main.locale.reference;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.reference.StringListReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LocaleStringListReference extends LocaleReference<List<String>> implements StringListReference<String> {

    private final List<String> empty = new ArrayList<>();

    public LocaleStringListReference(@NotNull String path) {
        super(fileConfiguration -> {
            List<String> list = fileConfiguration.getStringList(BosterParticles.getInstance().getLocalesManager().toPath(path));
            if(list.isEmpty()) return null;

            return list;
        });
    }

    @Override
    public @NotNull List<String> get(@NotNull String locale) {
        List<String> list = super.get(locale);
        return list != null ? list : empty;
    }
}
