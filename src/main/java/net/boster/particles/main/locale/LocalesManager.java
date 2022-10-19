package net.boster.particles.main.locale;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.security.CodeSource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RequiredArgsConstructor
@Getter
@Setter
public class LocalesManager {

    private static final YamlConfiguration defaults = new DefaultLocaleConfiguration();

    @NotNull private final BosterParticles plugin;
    @NotNull private Map<String, Locale> locales = new HashMap<>();
    @NotNull private Locale defaultLocale = new DefaultLocale();

    public void load() {
        locales = new HashMap<>();

        File folder = new File(plugin.getDataFolder() + "/locales");
        if(!folder.exists()) {
            folder.mkdirs();
        }

        if(folder.listFiles().length == 0) {
            loadDefaultLocales();
        }

        for(File f : folder.listFiles()) {
            try {
                if(!f.getName().endsWith(".yml")) continue;
                if(f.getName().equals(".yml")) continue;

                processLocale(f);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        setupDefaultLocale();
    }

    private void loadDefaultLocales() {
        CodeSource src = BosterParticles.class.getProtectionDomain().getCodeSource();
        if (src != null) {
            try {
                ZipInputStream zip = new ZipInputStream(src.getLocation().openStream());
                ZipEntry e;
                while((e = zip.getNextEntry()) != null) {
                    if(e.getName().startsWith("locales/") && !e.getName().equals("locales/") && e.getName().endsWith(".yml")) {
                        copyFile(e.getName().split("locales/")[1]);
                    }
                }
                zip.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void setupDefaultLocale() {
        defaultLocale = locales.getOrDefault(plugin.getConfig().getString("Settings.DefaultLocale", "").toLowerCase(), defaultLocale);
    }

    private void processLocale(File f) throws IOException, InvalidConfigurationException {
        YamlConfiguration c = new YamlConfiguration();
        c.addDefaults(defaults);
        c.load(f);
        Locale l = new Locale(f.getName().split(".yml")[0].toLowerCase(),
                c.getStringList("Aliases").stream().map(String::toLowerCase).collect(Collectors.toList()),
                f, c);
        locales.put(l.getId(), l);
        for(String s : l.getAliases()) {
            locales.put(s, l);
        }
    }

    private void copyFile(String path) {
        try {
            File f = new File(plugin.getDataFolder() + "/locales", path);
            Files.copy(plugin.getResource("locales/" + path), f.toPath());
            processLocale(f);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public @NotNull String toPath(@NotNull String s) {
        return s.replace("\\\\", ".").replace("//", ".")
                .replace("/", ".").replace("\\", ".");
    }

    private FileConfiguration getOrDef(String locale) {
        return locales.getOrDefault(toPath(locale), defaultLocale).getConfiguration();
    }

    public Set<String> getKeys(@NotNull String locale, boolean deep) {
        return getOrDef(locale).getKeys(deep);
    }

    public boolean contains(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).contains(path);
    }

    public Object get(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).get(path);
    }

    @Contract("_, _, !null -> !null")
    public Object get(@NotNull String locale, @NotNull String path, @Nullable Object def) {
        return getOrDef(locale).get(path, def);
    }

    public String getString(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getString(path);
    }

    @Contract("_, _, !null -> !null")
    public String getString(@NotNull String locale, @NotNull String path, String def) {
        return getOrDef(locale).getString(path, def);
    }

    public int getInt(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getInt(path);
    }

    public int getInt(@NotNull String locale, @NotNull String path, int def) {
        return getOrDef(locale).getInt(path, def);
    }

    public boolean getBoolean(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getBoolean(path);
    }

    public boolean getBoolean(@NotNull String locale, @NotNull String path, boolean def) {
        return getOrDef(locale).getBoolean(path, def);
    }

    public double getDouble(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getDouble(path);
    }

    public double getDouble(@NotNull String locale, @NotNull String path, double def) {
        return getOrDef(locale).getDouble(path, def);
    }

    public long getLong(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getLong(path);
    }

    public long getLong(@NotNull String locale, @NotNull String path, long def) {
        return getOrDef(locale).getLong(path, def);
    }

    public List<?> getList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getList(path);
    }

    @Contract("_, _, !null -> !null")
    public List<?> getList(@NotNull String locale, @NotNull String path, @Nullable List<?> def) {
        return getOrDef(locale).getList(path, def);
    }

    public List<String> getStringList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getStringList(path);
    }

    public List<Integer> getIntegerList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getIntegerList(path);
    }

    public List<Boolean> getBooleanList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getBooleanList(path);
    }

    public List<Double> getDoubleList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getDoubleList(path);
    }

    public List<Float> getFloatList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getFloatList(path);
    }

    public List<Long> getLongList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getLongList(path);
    }

    public List<Byte> getByteList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getByteList(path);
    }

    public List<Character> getCharacterList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getCharacterList(path);
    }

    public List<Short> getShortList(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getShortList(path);
    }

    public ConfigurationSection getConfigurationSection(@NotNull String locale, @NotNull String path) {
        return getOrDef(locale).getConfigurationSection(path);
    }

    static class DefaultLocale extends Locale {
        public DefaultLocale() {
            super("default", new ArrayList<>(), new YamlConfiguration());

            FileConfiguration c = getConfiguration();

            c.set("Messages.noPermission", "%prefix% &cYou don't have permission to use this command!");
            c.set("Messages.reload", "%prefix% &fThe plugin has been reloaded!");
        }
    }
}
