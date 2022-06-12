package net.boster.particles.main.gui.craft;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.gui.button.GUIButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CraftCustomGUI {

    private final HashMap<Integer, GUIButton> buttons = new HashMap<>();

    @Getter private int size = 9;
    @Getter @Setter @Nullable private String title;

    @Getter @Setter @Nullable private CraftGUIActions actions;

    public boolean accessPlayerInventory = false;
    public List<Integer> accessibleSlots = new ArrayList<>();

    public CraftCustomGUI(int size, @Nullable String title) {
        setSize(size);
        this.title = title;
    }

    public CraftCustomGUI(int size) {
        this(size, "BosterAPI-GUI");
    }

    public CraftCustomGUI(@Nullable String title) {
        this(9, title);
    }

    public CraftCustomGUI() {
        this(9, "BosterAPI-GUI");
    }

    public @NotNull Inventory getGUI(String title) {
        Inventory gui;
        if(title != null) {
            gui = Bukkit.createInventory(null, size, title);
        } else {
            gui = Bukkit.createInventory(null, size);
        }
        if(actions != null) {
            actions.onInventoryGeneration(gui);
        }
        return gui;
    }

    public @NotNull Inventory getGUI() {
        return getGUI(title);
    }

    public @Nullable GUIButton getButton(int i) {
        return buttons.get(i);
    }

    public void addButton(GUIButton button) {
        buttons.put(button.getSlot(), button);
    }

    public void removeButton(int i) {
        buttons.remove(i);
    }

    public void setClosed(Player p) {
        onClose(p);
    }

    public void onClose(Player p) {
        if(actions != null) {
            actions.onClose(p);
        }
    }

    public void onOpen(Player p) {
        if(actions != null) {
            actions.onOpen(p);
        }
    }

    public boolean setSize(int i) {
        try {
            Bukkit.createInventory(null, i);
            this.size = i;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Collection<GUIButton> getButtons() {
        return buttons.values();
    }

    public boolean checkAccess(int slot) {
        if(accessibleSlots == null) return false;

        return accessibleSlots.contains(slot);
    }
}
