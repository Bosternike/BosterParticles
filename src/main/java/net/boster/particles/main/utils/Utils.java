package net.boster.particles.main.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.colorutils.ColorUtils;
import net.boster.particles.main.utils.colorutils.NewColorUtils;
import net.boster.particles.main.utils.colorutils.OldColorUtils;
import net.boster.particles.main.utils.item.ItemManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class Utils {

    private static ColorUtils colorUtils;
    public static ItemStack SKULL;

    static {
        try {
            SKULL = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        } catch (Exception e) {
            SKULL = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        }

        try {
            ChatColor.class.getMethod("of", String.class);
            colorUtils = new NewColorUtils();
        } catch (NoSuchMethodException e) {
            colorUtils = new OldColorUtils();
        }
    }


    public static String toColor(String s) {
        if(s == null) return null;


        return colorUtils.toColor(s.replace("%prefix%", BosterParticles.getInstance().getLoader().getPrefix()));
    }

    public static int menuPages(int values, int from, int to) {
        return menuPages(values, to - from);
    }

    public static int menuPages(int values, int slots) {
        int pages;
        if(values <= slots) {
            pages = 1;
        } else {
            int sum = values / slots;
            pages = sum;
            if(values > slots * sum) {
                pages++;
            }
        }
        return pages;
    }

    public static int getGUIFirstSlotOfLastRaw(int slots) {
        if(slots < 9) {
            return 0;
        } else {
            return slots - 9;
        }
    }

    public static int[] createBorder(int slots) {
        if(slots == 27) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        } else if(slots == 36) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
        } else if(slots == 45) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        } else if(slots == 54) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        }

        return new int[]{};
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

        ItemManager manager = BosterParticles.getInstance().getLoader().getItemManager();

        ItemStack item;
        if(section.getString("head") != null) {
            item = getCustomSkull(toReplaces(section.getString("head"), replaces));
        } else if(section.getString("skull") != null) {
            item = SKULL.clone();
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            manager.setOwner(meta, toReplaces(section.getString("skull"), replaces));
            item.setItemMeta(meta);
        } else {
            try {
                item = manager.createItem(toReplaces(section.getString("material"), replaces));
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
        if(section.get("CustomModelData") != null) {
            manager.setCustomModelData(meta, section.getInt("CustomModelData"));
        }

        int damage = section.getInt("damage", -1);
        if(damage > -1) {
            item.setDurability((short) damage);
        }

        meta.addItemFlags(ItemFlag.values());

        item.setItemMeta(meta);
        return item;
    }

    public static String encode(Object s) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(s);
            outputStream.close();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }

    public static String decode(String s) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(s));
        try {
            BukkitObjectInputStream inputStream = new BukkitObjectInputStream(byteArrayInputStream);
            String r = inputStream.readObject().toString();
            inputStream.close();
            return r;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
