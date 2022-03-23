package net.boster.particles.main.gui;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.utils.LogType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ParticlesGUI {

    private static final HashMap<String, ParticlesGUI> hash = new HashMap<>();

    private final MenuFile file;
    private final String name;
    private String title;
    private final CraftCustomGUI gui;
    private final List<String> commands = new ArrayList<>();
    private final GUIPlaceholders placeholders = new GUIPlaceholders();
    private final List<ButtonItem> items = new ArrayList<>();
    private String permission;
    private String noPermissionMessage;
    private int size;

    public ParticlesGUI(MenuFile file) {
        this.file = file;
        this.name = file.getName();

        title = file.getFile().getString("Title", "&cNo title has been set yet.");

        if(!setSize(file.getFile().getInt("Size", 54))) {
            size = 54;
        }

        gui = new CraftCustomGUI(size, BosterParticles.toColor(title));

        for(String cmd : file.getFile().getStringList("Commands")) {
            ParticlesGUI g = getByCommand(cmd);
            if(g != null) {
                log("Command &9" + cmd + " &7can not be loaded due to this command has already been declared in menu \"&6" + g.getName() + "&7\".", LogType.INFO);
            } else {
                commands.add(cmd);
            }
        }

        ConfigurationSection items = file.getFile().getConfigurationSection("Items");
        if(items != null && items.getKeys(false).size() > 0) {
            for(String item : items.getKeys(false)) {
                List<Integer> slots = items.getIntegerList(item + ".slots");
                if(!slots.isEmpty()) {
                    for(int i : slots) {
                        ButtonItem b = ButtonItem.load(this, items.getConfigurationSection(item), i);
                        if(b != null) {
                            gui.addButton(b);
                            this.items.add(b);
                        }
                    }
                } else {
                    ButtonItem b = ButtonItem.load(this, items.getConfigurationSection(item));
                    if(b != null) {
                        gui.addButton(b);
                        this.items.add(b);
                    }
                }
            }
        } else {
            log("This gui is empty now. You should fill it.", LogType.INFO);
        }

        permission = file.getFile().getString("Permission");
        noPermissionMessage = file.getFile().getString("NoPermission");

        placeholders.setPlaceholder("ClickToActivate", "&aClick to activate", file.getFile().getString("Placeholders.ClickToActivate"));
        placeholders.setPlaceholder("NotPermitted", "&cYou do not have permission for this particle", file.getFile().getString("Placeholders.NotPermitted"));
        placeholders.setPlaceholder("AllowedStatus", "&aAllowed", file.getFile().getString("Placeholders.AllowedStatus"));
        placeholders.setPlaceholder("DeniedStatus", "&aDenied", file.getFile().getString("Placeholders.DeniedStatus"));

        hash.put(name, this);
        log("Loaded successfully!", LogType.FINE);
    }

    public MenuFile getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public static ParticlesGUI get(String name) {
        return hash.get(name);
    }

    public static ParticlesGUI getByCommand(String cmd) {
        for(ParticlesGUI gui : guis()) {
            if(gui.getCommands().contains(cmd)) {
                return gui;
            }
        }

        return null;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setTitle(String s) {
        title = s;
        gui.setTitle(s);
    }

    public void open(Player p) {
        Inventory g = gui.getGUI();

        for(ButtonItem item : items) {
            g.setItem(item.slot, item.prepareItem(p));
        }

        p.openInventory(g);

        Bukkit.getScheduler().scheduleSyncDelayedTask(BosterParticles.getInstance(), () -> gui.setOpened(p), 2);
    }

    public List<ButtonItem> getButtonItems() {
        return items;
    }

    public void log(String s, LogType log) {
        Bukkit.getConsoleSender().sendMessage(log.getFormat() + BosterParticles.toColor("(Menu: " + log.getColor() + name + "&7): " + s));
    }

    public void clear() {
        hash.remove(name);
    }

    public static Collection<ParticlesGUI> guis() {
        return hash.values();
    }

    public static void clearAll() {
        hash.clear();
    }

    public GUIPlaceholders getPlaceholders() {
        return placeholders;
    }

    public String getPermission() {
        return permission;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public void setPermission(String s) {
        permission = s;
    }

    public void setNoPermissionMessage(String s) {
        noPermissionMessage = s;
    }

    public boolean setSize(int i) {
        try {
            Bukkit.createInventory(null, i);
            size = i;
            return true;
        } catch (Exception e) {
            log("Could not set size to &c" + i, LogType.WARNING);
            return false;
        }
    }

    static class GUIPlaceholders {
        private final HashMap<String, GUIPlaceholder> placeholders = new HashMap<>();

        static class GUIPlaceholder {

            public final String notNull;
            public String nullable;

            public GUIPlaceholder(String notNull) {
                Validate.notNull(notNull);
                this.notNull = notNull;
            }

            public String get() {
                if(nullable == null) {
                    return notNull;
                } else {
                    return nullable;
                }
            }
        }

        public String getPlaceholder(String s) {
            return placeholders.get(s).get();
        }

        public GUIPlaceholder getGUIPlaceholder(String s) {
            return placeholders.get(s);
        }

        public void setPlaceholder(String placeholder, String notNull, String nullable) {
            GUIPlaceholder pp = new GUIPlaceholder(notNull);
            pp.nullable = nullable;
            placeholders.put(placeholder, pp);
        }

        public void setGUIPlaceholder(String placeholder, GUIPlaceholder gp) {
            placeholders.put(placeholder, gp);
        }

        public void clear() {
            placeholders.clear();
        }
    }
}
