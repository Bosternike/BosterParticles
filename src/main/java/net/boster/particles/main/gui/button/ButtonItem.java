package net.boster.particles.main.gui.button;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.lib.PAPISupport;
import net.boster.particles.main.lib.VaultSupport;
import net.boster.particles.main.utils.BosterSound;
import net.boster.particles.main.utils.CachedSetSection;
import net.boster.particles.main.utils.LogType;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ButtonItem implements GUIButton {

    public final @NotNull ParticlesGUI gui;
    public final @NotNull ConfigurationSection section;
    public final @NotNull String name;

    public @Nullable String permission;
    public double price = -1;

    public final int slot;
    public @Nullable final ItemStack itemStack;

    public @Nullable String itemName;
    public @Nullable List<String> lore;

    public boolean hasCustomModelData;
    public int customModelData;

    public @Nullable ButtonAction successfulActions;
    public @Nullable ButtonAction noPermissionActions;
    public @Nullable ButtonAction notEnoughMoneyActions;

    public ButtonItem(@NotNull ParticlesGUI gui, @NotNull ConfigurationSection item, int slot, @Nullable ItemStack itemStack) {
        this.gui = gui;
        this.section = item;
        this.name = item.getName();
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public static ButtonItem load(@NotNull ParticlesGUI gui, @Nullable ConfigurationSection item, int slot) {
        return load(gui, item, slot, false);
    }

    public static ButtonItem load(@NotNull ParticlesGUI gui, @Nullable ConfigurationSection item, int slot, boolean allowOverride) {
        if(item == null) return null;

        if(!checkSize(gui, item, slot)) return null;
        if(!checkOverrides(gui, item, slot, allowOverride)) return null;

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
                String m = item.getString("material");
                try {
                    material = BosterParticles.getInstance().getLoader().getItemCreator().createItem(m);
                } catch (Exception e) {
                    gui.log("Item \"&6" + item.getName() + "&7\" has invalid material type \"&c" + m + "&7\"", LogType.WARNING);
                    return null;
                }
            }
        }

        ButtonItem b = new ButtonItem(gui, item, slot, material);

        b.permission = item.getString("permission");
        b.price = item.getDouble("cost", -1);

        b.itemName = item.getString("name");
        b.lore = item.getStringList("lore");

        b.hasCustomModelData = item.getString("CustomModelData") != null;
        if(b.hasCustomModelData) {
            b.customModelData = item.getInt("CustomModelData");
        }

        b.successfulActions = ClickActions.load(item.getConfigurationSection("success_actions"), b);
        b.noPermissionActions = ClickActions.load(item.getConfigurationSection("no_permission_actions"), b);
        b.notEnoughMoneyActions = ClickActions.load(item.getConfigurationSection("not_enough_money_actions"), b);

        return b;
    }

    private static boolean checkOverrides(@NotNull ParticlesGUI gui, @Nullable ConfigurationSection item, int slot, boolean allowOverride) {
        if(!allowOverride) {
            for(GUIButton i : gui.getGui().getButtons()) {
                if(i.getSlot() == slot) {
                    String name = i instanceof ButtonItem b ? b.name : "id not found";
                    gui.log("Item \"&6" + item.getName() + "&7\" tried to override slot of item \"&e" + name + "&7\"", LogType.WARNING);
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean checkSize(@NotNull ParticlesGUI gui, @Nullable ConfigurationSection item, int slot) {
        if(slot >= gui.getSize()) {
            gui.log("Item \"&6" + item.getName() + "&7\" slot is &c" + slot + "&7, but GUI size is &e" + gui.getSize(), LogType.WARNING);
            return false;
        }

        return true;
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

        double bal = VaultSupport.getBalance(p);
        if(price > -1 && bal < price) {
            if(notEnoughMoneyActions != null) {
                notEnoughMoneyActions.act(p, "%cost%", Integer.toString((int) price), "%need%", Integer.toString((int) (price - bal)));
            }
            return;
        }

        if(successfulActions != null) {
            successfulActions.act(p);
        }
    }

    public ItemStack prepareItem(Player p) {
        if(itemStack != null) {
            ItemStack item = itemStack.clone();
            ItemMeta meta = item.getItemMeta();

            if(itemName != null) {
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
        String r = PAPISupport.setPlaceholders(p, toSystemReplaces(p, s));
        r = r.replace("%do_click%", b1 ? gui.getPlaceholders().getPlaceholder("ClickToActivate") :
                        gui.getPlaceholders().getPlaceholder("NotPermitted"))
                .replace("%status%", b1 ? gui.getPlaceholders().getPlaceholder("AllowedStatus") :
                        gui.getPlaceholders().getPlaceholder("DeniedStatus"));
        return BosterParticles.toColor(r);
    }

    public static class ClickActions implements ButtonAction {

        public final ButtonItem item;

        public final BosterSound sound;
        public final List<String> messages;
        public final List<String> commands;
        public final List<String> announce;

        public final CachedSetSection set;

        public ClickActions(@NotNull ButtonItem i, @Nullable BosterSound sound, @NotNull List<String> messages, @NotNull List<String> commands, @NotNull List<String> announce, @NotNull CachedSetSection set) {
            this.item = i;
            this.sound = sound;
            this.messages = messages;
            this.commands = commands;
            this.announce = announce;
            this.set = set;
        }

        public static @NotNull ClickActions load(ConfigurationSection section, ButtonItem b) {
            if(section == null) {
                return new ClickActions(b, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new CachedSetSection());
            }

            return new ClickActions(b, BosterSound.load(section.getString("sound")), section.getStringList("message"),
                    section.getStringList("commands"), section.getStringList("announce"),
                    CachedSetSection.load(section, "Could not load from bytes! Gui: \"&c" + b.gui.getName() + "&7\"; Item: \"&c" + b.section.getName() + "&7\"; " +
                            "Path: \"&c" + b.section.getCurrentPath() + "&7\"."));
        }

        public void act(@NotNull Player p, @Nullable String... replaces) {
            if(sound != null) {
                p.playSound(p.getLocation(), sound.sound, 1, sound.i);
            }
            messages.forEach(s -> p.sendMessage(item.toReplaces(p, Utils.toReplaces(s, replaces))));
            commands.forEach(s -> p.chat(item.toSystemReplaces(p, Utils.toReplaces(s, replaces))));
            announce.forEach(s -> Bukkit.broadcastMessage(item.toReplaces(p, Utils.toReplaces(s, replaces))));

            PlayerData data = PlayerData.get(p);
            boolean ld = false;

            if(set.clearFile) {
                data.clearFile();
                ld = true;
            }
            for(String s : set.clearSections) {
                data.clearSection(s);
                ld = true;
            }
            if(set.setToFile != null && set.setToFile.getKeys(false).size() > 0) {
                set.setToFile.getKeys(false).forEach(sc -> data.data.set(sc, set.setToFile.getConfigurationSection(sc)));
                ld = true;
            }
            if(ld) {
                data.update();
            }
        }
    }
}
