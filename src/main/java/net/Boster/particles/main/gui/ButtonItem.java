package net.Boster.particles.main.gui;

import net.Boster.particles.main.BosterParticles;
import net.Boster.particles.main.data.PlayerData;
import net.Boster.particles.main.lib.PAPISupport;
import net.Boster.particles.main.utils.BosterSound;
import net.Boster.particles.main.utils.LogType;
import net.Boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ButtonItem implements GUIButton {

    public final ParticlesGUI gui;
    public final ConfigurationSection section;
    public final String name;

    public String permission;

    public final int slot;
    public final ItemStack itemStack;

    public String itemName;
    public List<String> lore;

    public boolean hasCustomModelData;
    public int customModelData;

    public ClickActions successfulActions;
    public ClickActions noPermissionActions;

    public ButtonItem(ParticlesGUI gui, ConfigurationSection item, int slot, ItemStack itemStack) {
        this.gui = gui;
        this.section = item;
        this.name = item.getName();
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public static ButtonItem load(ParticlesGUI gui, ConfigurationSection item, int slot) {
        return load(gui, item, slot, false);
    }

    public static ButtonItem load(ParticlesGUI gui, ConfigurationSection item, int slot, boolean allowOverride) {
        if(!allowOverride) {
            for(ButtonItem i : gui.getButtonItems()) {
                if(i.slot == slot) {
                    gui.log("Item \"&6" + item.getName() + "&7\" tried to override slot of item \"&e" + i.name + "&7\"", LogType.WARNING);
                    return null;
                }
            }
        }

        ItemStack material = null;
        if(!item.getBoolean("blank", false)) {
            if(item.getString("head") != null) {
                material = Utils.getCustomSkull(item.getString("head"));
            } else if(item.getString("skull") != null) {
                material = Utils.SKULL.clone();
                SkullMeta meta = (SkullMeta) material.getItemMeta();
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(item.getString("skull")));
                material.setItemMeta(meta);
            } else {
                try {
                    material = new ItemStack(Material.valueOf(item.getString("material")));
                } catch (Exception e) {
                    return null;
                }
            }
        }

        ButtonItem b = new ButtonItem(gui, item, slot, material);

        b.permission = item.getString("permission");

        b.itemName = item.getString("name");
        b.lore = item.getStringList("lore");

        b.hasCustomModelData = item.getString("CustomModelData") != null;
        if(b.hasCustomModelData) {
            b.customModelData = item.getInt("CustomModelData");
        }

        b.successfulActions = ClickActions.load(item.getConfigurationSection("success_actions"), b);
        b.noPermissionActions = ClickActions.load(item.getConfigurationSection("no_permission_actions"), b);

        return b;
    }

    public static ButtonItem load(ParticlesGUI gui, ConfigurationSection section) {
        return load(gui, section, section.getInt("slot", 0));
    }

    public static ButtonItem load(ParticlesGUI gui, ConfigurationSection section, boolean allowOverride) {
        return load(gui, section, section.getInt("slot", 0), allowOverride);
    }

    @Override
    public int getSlot() {
        return slot;
    }

    @Override
    public void onClick(Player p) {
        if(permission != null && !p.hasPermission(permission)) {
            if(noPermissionActions != null) {
                noPermissionActions.act(p);
            }
            return;
        }

        if(successfulActions != null) {
            successfulActions.act(p);
        }
    }

    public ItemStack prepareItem(Player p) {
        if(!isBlank()) {
            ItemStack item = itemStack.clone();
            ItemMeta meta = item.getItemMeta();

            if(name != null) {
                meta.setDisplayName(toReplaces(p, itemName));
            }
            if(lore != null) {
                List<String> lr = new ArrayList<>();
                lore.forEach(l -> lr.add(toReplaces(p, l)));
                meta.setLore(lr);
            }

            if(hasCustomModelData) {
                meta.setCustomModelData(customModelData);
            }

            item.setItemMeta(meta);

            return item;
        }

        return null;
    }

    public boolean isBlank() {
        return itemStack == null;
    }

    public String toSystemReplaces(Player p, String s) {
        return s.replace("%player%", p.getName());
    }

    public String toReplaces(Player p, String s) {
        boolean b1 = permission != null && p.hasPermission(permission);
        s = PAPISupport.setPlaceholders(p, toSystemReplaces(p, s)).replace("%do_click%", b1 ? gui.getPlaceholders().getPlaceholder("ClickToActivate") :
                gui.getPlaceholders().getPlaceholder("NotPermitted"))
                .replace("%status%", b1 ? gui.getPlaceholders().getPlaceholder("AllowedStatus") :
                        gui.getPlaceholders().getPlaceholder("DeniedStatus"));
        return BosterParticles.toColor(s);
    }

    public static class ClickActions {

        public final ButtonItem item;

        public ClickActions(ButtonItem i) {
            this.item = i;
        }

        public BosterSound sound;
        public List<String> messages;
        public List<String> commands;
        public List<String> announce;

        public boolean clearFile = false;
        public List<String> clearSections = new ArrayList<>();
        public ConfigurationSection setToFile;

        public static @NotNull ClickActions load(ConfigurationSection section, ButtonItem i) {
            if(section == null) {
                return new ClickActions(i);
            }

            ClickActions a = new ClickActions(i);
            a.sound = BosterSound.load(section.getString("sound"));
            a.messages = section.getStringList("message");
            a.commands = section.getStringList("commands");
            a.announce = section.getStringList("announce");

            a.setToFile = section.getConfigurationSection("SetToFile");

            a.clearFile = section.getBoolean("ClearFile", false);
            a.clearSections = section.getStringList("ClearSections");
            return a;
        }

        public void act(Player p) {
            if(sound != null) {
                p.playSound(p.getLocation(), sound.sound, 1, sound.i);
            }
            if(messages != null && !messages.isEmpty()) {
                for(String s : messages) {
                    p.sendMessage(item.toReplaces(p, s));
                }
            }
            if(commands != null && !commands.isEmpty()) {
                for(String s : commands) {
                    p.chat(item.toSystemReplaces(p, s));
                }
            }
            if(announce != null && !announce.isEmpty()) {
                for(String s : announce) {
                    Bukkit.broadcastMessage(item.toReplaces(p, s));
                }
            }

            PlayerData data = PlayerData.get(p);
            boolean ld = false;

            if(clearFile) {
                data.clearFile();
                ld = true;
            }
            for(String s : clearSections) {
                data.clearSection(s);
                ld = true;
            }
            if(setToFile != null && setToFile.getKeys(false).size() > 0) {
                FileConfiguration file = data.data;
                for(String sc : setToFile.getKeys(false)) {
                    file.set(sc, setToFile.getConfigurationSection(sc));
                }
                ld = true;
            }
            if(ld) {
                data.update();
            }
        }
    }
}
