package net.boster.particles.main.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;

public class ConfigUtils {

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

    public static boolean hasAllStrings(@NotNull ConfigurationSection instance, @NotNull ConfigurationSection toCheck) {
        for(String k : instance.getKeys(false)) {
            if(toCheck.get(k) == null) return false;

            ConfigurationSection ns = instance.getConfigurationSection(k);
            if(ns != null) {
                ConfigurationSection nc = toCheck.getConfigurationSection(k);
                if(nc == null) {
                    return false;
                } else {
                    if(!hasAllStrings(ns, nc)) {
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
}
