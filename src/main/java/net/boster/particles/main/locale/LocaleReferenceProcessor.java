package net.boster.particles.main.locale;

import lombok.RequiredArgsConstructor;
import net.boster.particles.main.locale.reference.LocaleStringListReference;
import net.boster.particles.main.locale.reference.LocaleStringReference;
import net.boster.particles.main.utils.Utils;
import net.boster.particles.main.utils.reference.StringListReference;
import net.boster.particles.main.utils.reference.StringReference;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LocaleReferenceProcessor {

    public static @NotNull StringReference<@NotNull String> processStringReference(@NotNull ConfigurationSection section, @NotNull String path) {
        return processStringElementReference(section.getString(path));
    }

    public static @NotNull StringReference<@NotNull String> processStringElementReference(@Nullable String element) {
        if(element == null) return new SimpleStringReference("");

        String[] ss = element.split("reference:");
        if(ss.length == 0) {
            return new SimpleStringReference(element);
        } else {
            try {
                return new LocaleStringReference(ss[1]);
            } catch (Exception e) {
                return new SimpleStringReference(element);
            }
        }
    }

    public static @NotNull StringListReference<@NotNull String> processStringListReference(@NotNull ConfigurationSection section, @NotNull String path) {
        if(!section.contains(path)) return new SimpleStringListReference();

        String s;
        List<String> list = section.getStringList(path);
        if(!list.isEmpty()) {
            return processStringListReference(list);
        } else {
            s = section.getString(path);
        }

        if(s == null) return new SimpleStringListReference();

        String[] ss = s.split("reference:");
        if(ss.length == 0) {
            return new SimpleStringListReference();
        } else {
            try {
                return new LocaleStringListReference(ss[1]);
            } catch (Exception e) {
                return new SimpleStringListReference();
            }
        }
    }

    public static StringListReference<@NotNull String> processStringListReference(@NotNull List<String> list) {
        String[] ss = list.get(0).split("reference:");
        if(ss.length == 0) {
            return new SimpleStringListReference(list);
        } else {
            try {
                return new LocaleStringListReference(ss[1]);
            } catch (Exception e) {
                return new SimpleStringListReference(list);
            }
        }
    }

    static class SimpleStringReference implements StringReference<String> {

        @NotNull private final String string;

        public SimpleStringReference(@NotNull String s) {
            string = Utils.toColor(s);
        }

        @Override
        public String get(@NotNull String s) {
            return string;
        }
    }

    static class SimpleStringListReference implements StringListReference<String> {

        @NotNull private final List<String> list;

        public SimpleStringListReference(@NotNull List<String> list) {
            this.list = list.stream().map(Utils::toColor).collect(Collectors.toList());
        }

        public SimpleStringListReference() {
            this(new ArrayList<>());
        }

        @Override
        public @NotNull List<String> get(@NotNull String s) {
            return list;
        }
    }
}
