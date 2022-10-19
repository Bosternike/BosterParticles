package net.boster.particles.main.locale.reference;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.locale.Locale;
import net.boster.particles.main.locale.LocalesManager;
import net.boster.particles.main.utils.reference.Reference;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LocaleReference<T> implements Reference<T, String> {

    private final Map<String, T> map = new HashMap<>();

    public LocaleReference(@NotNull Function<@NotNull FileConfiguration, T> function) {
        LocalesManager locales = BosterParticles.getInstance().getLocalesManager();

        for(Locale l : locales.getLocales().values()) {
            T t = function.apply(l.getConfiguration());
            if(t == null) {
                t = function.apply(locales.getDefaultLocale().getConfiguration());
            }
            if(t != null) {
                map.put(l.getId(), t);
                for(String s : l.getAliases()) {
                    map.put(s, t);
                }
            }
        }
    }

    @Override
    public T get(@NotNull String locale) {
        T t = map.get(locale);
        if(t == null) {
            t = map.get(BosterParticles.getInstance().getLocalesManager().getDefaultLocale().getId());
        }
        return t;
    }
}
