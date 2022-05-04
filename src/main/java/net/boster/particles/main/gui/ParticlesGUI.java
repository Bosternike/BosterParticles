package net.boster.particles.main.gui;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.commands.PGCommand;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.button.ButtonItem;
import net.boster.particles.main.gui.craft.CraftCustomGUI;
import net.boster.particles.main.gui.placeholders.GUIPlaceholders;
import net.boster.particles.main.utils.log.LogType;
import net.boster.particles.main.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ParticlesGUI {

    private static final HashMap<String, ParticlesGUI> hash = new HashMap<>();

    @Getter @NotNull private final MenuFile file;
    @Getter @NotNull private final String name;
    @Getter @NotNull private final CraftCustomGUI gui;
    @Getter @Nullable private PGCommand command;
    @Getter private final List<String> commands = new ArrayList<>();
    @Getter @NotNull private final GUIPlaceholders placeholders = new GUIPlaceholders();
    @Getter @Setter @Nullable private String permission;
    @Getter @Setter @Nullable private String noPermissionMessage;

    public ParticlesGUI(@NotNull MenuFile file) {
        this.file = file;
        this.name = file.getName();

        gui = new CraftCustomGUI(BosterParticles.toColor(file.getConfig().getString("Title", "&cNo title has been set yet.")));

        setSize(file.getConfig().getInt("Size", 54));

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
            this.command = new PGCommand(this);
        }

        ConfigurationSection items = file.getConfig().getConfigurationSection("Items");
        if(items != null && items.getKeys(false).size() > 0) {
            for(String item : items.getKeys(false)) {
                List<Integer> slots = items.getIntegerList(item + ".slots");
                if(!slots.isEmpty()) {
                    for(int i : slots) {
                        if(i >= gui.getSize()) {
                            log("Item \"&6" + item + "&7\" slot is &c" + i + "&7, but GUI size is &e" + gui.getSize(), LogType.WARNING);
                            continue;
                        }

                        ButtonItem b = ButtonItem.load(this, items.getConfigurationSection(item), i);
                        if(b != null) {
                            gui.addButton(b);
                        }
                    }
                } else {
                    ButtonItem b = ButtonItem.load(this, items.getConfigurationSection(item));
                    if(b != null) {
                        gui.addButton(b);
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
        gui.setTitle(s);
    }

    public void open(Player p) {
        CustomGUI cg = new CustomGUI(p, gui);
        cg.open();
    }

    public void log(String s, LogType log) {
        Bukkit.getConsoleSender().sendMessage(log.getFormat() + BosterParticles.toColor("(Menu: " + log.getColor() + name + "&7): " + s));
    }


    public void delete() {
        file.getFile().delete();
        clear();
    }

    public void clear() {
        if(command != null) {
            command.unregister();
            ReflectionUtils.syncCommands();
        }
        file.clear();
        hash.remove(name);
    }

    public static Collection<ParticlesGUI> guis() {
        return hash.values();
    }

    public static void clearAll() {
        hash.clear();
    }

    public int getSize() {
        return gui.getSize();
    }

    public @NotNull String getTitle() {
        return gui.getTitle();
    }

    public boolean setSize(int i) {
        try {
            Bukkit.createInventory(null, i);
            gui.setSize(i);
            return true;
        } catch (Exception e) {
            log("Could not set size to &c" + i, LogType.WARNING);
            return false;
        }
    }
}
