package net.boster.particles.main.utils;

import com.google.common.collect.Lists;
import net.boster.particles.main.files.CustomFile;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ConfigUtils {

    public static CustomFile createMenu(@NotNull String name) {
        CustomFile f = new CustomFile(name);
        f.setDir("menus/");
        f.createNewFile();
        f.getConfiguration().set("Title", "No title yet");
        f.getConfiguration().set("Permission", "no.permission.set.yet");
        f.getConfiguration().set("NoPermission", "No message set yet");
        f.getConfiguration().set("Commands", Lists.newArrayList("no-commands-set-yet"));
        f.getConfiguration().set("Placeholders.ClickToActivate", "&aClick to activate!");
        f.getConfiguration().set("Placeholders.NotPermitted", "&cYou don't have permission for this particle.");
        f.getConfiguration().set("Placeholders.AllowedStatus", "&aAble");
        f.getConfiguration().set("Placeholders.DeniedStatus", "&cUnable");
        f.getConfiguration().set("Size", 45);
        f.save();
        return f;
    }

    public static String toSimpleName(File f) {
        String name;
        try {
            String[] ss = f.getName().replace(".", ",").split(",");
            name = ss[ss.length - 2];
        } catch (Exception e) {
            name = f.getName();
        }
        return name;
    }

    public static boolean hasAllStrings(@NotNull ConfigurationSection instance, @NotNull ConfigurationSection toCheck, List<String> skip) {
        String parent = instance.getCurrentPath() != null && !instance.getCurrentPath().isEmpty() ? instance.getCurrentPath() + "." : "";

        for(String k : instance.getKeys(false)) {
            if(skip.contains(parent + k)) continue;
            if(toCheck.get(k) == null) return false;

            ConfigurationSection ns = instance.getConfigurationSection(k);
            if(ns != null) {
                ConfigurationSection nc = toCheck.getConfigurationSection(k);
                if(nc == null) {
                    return false;
                } else {
                    if(!hasAllStrings(ns, nc, skip)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void replaceOldConfig(@NotNull File oldFile, @NotNull File outFile, @NotNull InputStream config) {
        replaceOldConfig(oldFile, toSimpleName(oldFile), outFile, config);
    }

    public static void replaceOldConfig(@NotNull File oldFile, @NotNull String oldFileName, @NotNull File outFile, @NotNull InputStream config) {
        try {
            Files.move(oldFile.toPath(), new File(oldFile.getParentFile(), oldFileName + "-old_" + System.currentTimeMillis() + ".yml").toPath());
            saveResource(config, outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveResource(@NotNull InputStream in, File outFile) throws IOException {
        if(!outFile.exists()) {
            outFile.getParentFile().mkdirs();
            outFile.createNewFile();
        }

        OutputStream out = new FileOutputStream(outFile);
        byte[] buf = new byte[1024];

        int len;
        while((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        out.close();
        in.close();
    }

    public static List<String> readSection(@NotNull ConfigurationSection section) {
        return readSection(section, (s) -> s + ": ", (s) -> s);
    }

    public static List<String> readSection(@NotNull ConfigurationSection section, @NotNull Function<String, String> value) {
        return readSection(section, null, null, null, (s) -> s + "  ", (s) -> s + ": ", value);
    }

    public static List<String> readSection(@NotNull ConfigurationSection section, @NotNull Function<String, String> path, @NotNull Function<String, String> value) {
        return readSection(section, null, null, null, (s) -> s + "  ", path, value);
    }

    /**
     *
     * @param section Section to be read.
     * @param path Path will be replaced by your function.
     * @param spacesFun Add spaces as much as you want and use any symbol.
     * @param value Value will be replaced by your function.
     */
    public static List<String> readSection(@NotNull ConfigurationSection section, @NotNull Function<String, String> spacesFun, @NotNull Function<String, String> path, @NotNull Function<String, String> value) {
        return readSection(section, null, null, null, spacesFun, path, value);
    }

    private static List<String> readSection(@NotNull ConfigurationSection section, @Nullable String currentTreePath, @Nullable String spaces, @Nullable String newPath, @NotNull Function<String, String> spacesFun, @NotNull Function<String, String> path, @NotNull Function<String, String> value) {
        List<String> list = new ArrayList<>();

        String cs = spaces != null ? spaces : "";

        if(newPath != null) {
            list.add(cs + path.apply(newPath));
            cs = spacesFun.apply(cs);
        }

        for(String s : section.getKeys(false)) {
            ConfigurationSection c = section.getConfigurationSection(s);
            if(c != null) {
                list.addAll(readSection(c, currentTreePath != null ? currentTreePath + "." + s : s, cs, s, spacesFun, path, value));
            } else {
                String ps = cs + path.apply(s);
                if(section.get(s) instanceof List) {
                    list.add(ps);
                    for(Object o : (List<Object>) section.get(s)) {
                        String ls = value.apply(o.toString());
                        if(!(o instanceof Number)) {
                            if(ls.contains("'")) {
                                ls = "\u00a7f\"" + ls + "\u00a7f\"";
                            } else {
                                ls = "\u00a7f'" + ls + "\u00a7f'";
                            }
                        }
                        list.add(cs + "\u00a77- " + ls);
                    }
                } else {
                    list.add(ps + value.apply(section.getString(s)));
                }
            }
        }

        return list;
    }
}
