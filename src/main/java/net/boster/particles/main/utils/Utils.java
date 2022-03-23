package net.boster.particles.main.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.boster.particles.main.BosterParticles;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern pattern;
    public static ItemStack SKULL;

    static {
        try {
            SKULL = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        } catch (Exception e) {
            SKULL = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        }

        pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
    }

    public static String toColor(String s) {
        if(s == null) return null;

        String str = s;
        Matcher match = pattern.matcher(str);

        while(match.find()) {
            String color = s.substring(match.start() + 1, match.end());
            str = str.replace("&" + color, ChatColor.of(color) + "");
            match = pattern.matcher(str);
        }
        return ChatColor.translateAlternateColorCodes('&', str.replace("%prefix%", BosterParticles.getInstance().getConfig().getString("Settings.Prefix")));
    }

    public static String toReplaces(String s, String... replaces) {
        if(s == null) return null;

        String r = s;
        if(replaces != null) {
            for(int i = 0; i < replaces.length; i++) {
                if(i + 1 < replaces.length) {
                    r = r.replace(replaces[i], replaces[i + 1]);
                }
                i++;
            }
        }

        return r;
    }

    public static ItemStack getCustomSkull(String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));

        ItemStack skull = SKULL.clone();
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static ItemStack getItemStack(ConfigurationSection section) {
        return getItemStack(section, new String[]{});
    }

    public static ItemStack getItemStack(ConfigurationSection section, String... replaces) {
        if(section == null) return null;

        ItemStack item;
        if(section.getString("head") != null) {
            item = getCustomSkull(toReplaces(section.getString("head"), replaces));
        } else if(section.getString("skull") != null) {
            item = SKULL.clone();
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(toReplaces(section.getString("skull"), replaces)));
            item.setItemMeta(meta);
        } else {
            try {
                item = new ItemStack(Material.valueOf(toReplaces(section.getString("material"), replaces)));
            } catch (Exception e) {
                return null;
            }
        }
        ItemMeta meta = item.getItemMeta();
        if(section.getString("name") != null) {
            meta.setDisplayName(toColor(toReplaces(section.getString("name"), replaces)));
        }
        List<String> lore = new ArrayList<>();
        section.getStringList("lore").forEach(s -> lore.add(toColor(toReplaces(s, replaces))));
        meta.setLore(lore);
        if(section.getString("CustomModelData") != null) {
            meta.setCustomModelData(section.getInt("CustomModelData"));
        }
        item.setItemMeta(meta);
        return item;
    }
}
