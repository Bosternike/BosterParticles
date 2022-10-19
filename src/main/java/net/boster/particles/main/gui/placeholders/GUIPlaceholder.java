package net.boster.particles.main.gui.placeholders;

import net.boster.particles.main.utils.reference.StringReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GUIPlaceholder {

    @NotNull public final String notNull;
    @Nullable public StringReference<String> nullable;

    public GUIPlaceholder(@NotNull String notNull) {
        this.notNull = notNull;
    }

    public @NotNull String get(@NotNull String locale) {
        if(nullable == null) {
            return notNull;
        } else {
            String s = nullable.get(locale);
            return s != null ? s : notNull;
        }
    }
}
