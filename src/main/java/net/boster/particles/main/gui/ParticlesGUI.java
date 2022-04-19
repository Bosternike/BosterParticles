package net.boster.particles.main.gui;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.commands.PGCommand;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.utils.LogType;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ParticlesGUI {

    private static final HashMap<String, ParticlesGUI> hash = new HashMap<>();

    @Getter private final MenuFile file;
    @Getter private final String name;
    @Getter @NotNull private String title;
    @Getter private final CraftCustomGUI gui;
    @Getter private final List<String> commands = new ArrayList<>();
    @Getter private final GUIPlaceholders placeholders = new GUIPlaceholders();
    @Getter private final List<ButtonItem> items = new ArrayList<>();
    @Getter @Setter @Nullable private String permission;
    @Getter @Setter @Nullable private String noPermissionMessage;
    @Getter private int size;

    public ParticlesGUI(@NotNull MenuFile file) {
        this.file = file;
        this.name = file.getName();

        title = file.getConfig().getString("Title", "&cNo title has been set yet.");

        if(!setSize(file.getConfig().getInt("Size", 54))) {
            size = 54;
        }

        gui = new CraftCustomGUI(size, BosterParticles.toColor(title));

        for(String cmd : file.getConfig().getStringList("Commands")) {
            cmd = cmd.replaceFirst("/", "").toLowerCase();
            ParticlesGUI g = getByCommand(cmd);
            if(g != null) {
                log("Command &9" + cmd + " &7can not be loaded due to this command has already been declared in menu \"&6" + g.getName() + "&7\".", LogType.INFO);
            } else {
                commands.add(cmd);
            }
        }

        if(!commands.isEmpty()) {
            new PGCommand(this);
        }

        ConfigurationSection items = file.getConfig().getConfigurationSection("Items");
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

        permission = file.getConfig().getString("Permission");
        noPermissionMessage = file.getConfig().getString("NoPermission");

        placeholders.setPlaceholder("ClickToActivate", "&aClick to activate", file.getConfig().getString("Placeholders.ClickToActivate"));
        placeholders.setPlaceholder("NotPermitted", "&cYou do not have permission for this particle", file.getConfig().getString("Placeholders.NotPermitted"));
        placeholders.setPlaceholder("AllowedStatus", "&aAllowed", file.getConfig().getString("Placeholders.AllowedStatus"));
        placeholders.setPlaceholder("DeniedStatus", "&aDenied", file.getConfig().getString("Placeholders.DeniedStatus"));

        hash.put(name, this);
        log("Loaded successfully!", LogType.FINE);
    }

    public static ParticlesGUI get(String name) {
        return hash.get(name);
    }

    public static ParticlesGUI getByCommand(String cmd) {
        for(ParticlesGUI gui : guis()) {
            for(String c : gui.getCommands()) {
                if(c.equalsIgnoreCase(cmd)) return gui;
            }
        }

        return null;
    }

    public void setTitle(@NotNull String s) {
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
