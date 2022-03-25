package net.boster.particles.main.gui;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.lib.PAPISupport;
import net.boster.particles.main.utils.BosterSound;
import net.boster.particles.main.utils.LogType;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
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
                    material = BosterParticles.getInstance().getLoader().getItemCreator().createItem(item.getString("material"));
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
        String r = PAPISupport.setPlaceholders(p, toSystemReplaces(p, s)).replace("%do_click%", b1 ? gui.getPlaceholders().getPlaceholder("ClickToActivate") :
                gui.getPlaceholders().getPlaceholder("NotPermitted"))
                .replace("%status%", b1 ? gui.getPlaceholders().getPlaceholder("AllowedStatus") :
                        gui.getPlaceholders().getPlaceholder("DeniedStatus"));
        return BosterParticles.toColor(r);
    }

    public static class ClickActions {

        public final ButtonItem item;

        public final BosterSound sound;
        public final List<String> messages;
        public final List<String> commands;
        public final List<String> announce;

        public final boolean clearFile;
        public final List<String> clearSections;
        public final ConfigurationSection setToFile;

        public ClickActions(ButtonItem i, BosterSound sound, List<String> messages, List<String> commands, List<String> announce, boolean clearFile, List<String> clearSections, ConfigurationSection setToFile) {
            this.item = i;
            this.sound = sound;
            this.messages = messages;
            this.commands = commands;
            this.announce = announce;
            this.clearFile = clearFile;
            this.clearSections = clearSections;
            this.setToFile = setToFile;
        }

        public static @NotNull ClickActions load(ConfigurationSection section, ButtonItem b) {
            if(section == null) {
                return new ClickActions(b, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), false, new ArrayList<>(), null);
            }

            return new ClickActions(b, BosterSound.load(section.getString("sound")), section.getStringList("message"),
                    section.getStringList("commands"), section.getStringList("announce"),
                    section.getBoolean("ClearFile", false), section.getStringList("ClearSections"),
                    section.getConfigurationSection("SetToFile"));
        }

        public void act(Player p) {
            if(sound != null) {
                p.playSound(p.getLocation(), sound.sound, 1, sound.i);
            }
            messages.forEach(s -> p.sendMessage(item.toReplaces(p, s)));
            commands.forEach(s -> p.chat(item.toSystemReplaces(p, s)));
            announce.forEach(s -> Bukkit.broadcastMessage(item.toReplaces(p, s)));

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
                setToFile.getKeys(false).forEach(sc -> data.data.set(sc, setToFile.getConfigurationSection(sc)));
                ld = true;
            }
            if(ld) {
                data.update();
            }
        }
    }
}
