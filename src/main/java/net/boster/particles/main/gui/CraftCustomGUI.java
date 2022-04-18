package net.boster.particles.main.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CraftCustomGUI {

    public static final HashMap<Player, CraftCustomGUI> openedMap = new HashMap<>();

    private final HashMap<Integer, GUIButton> buttons = new HashMap<>();

    private int slots;
    private String title;

    private CraftGUIActions actions;

    public boolean accessPlayerInventory = false;
    public List<Integer> accessibleSlots = new ArrayList<>();

    public CraftCustomGUI(int slots, String title) {
        this.slots = slots;
        this.title = title;
    }

    public CraftCustomGUI(int slots) {
        this(slots, "BosterParticles");
    }

    public CraftCustomGUI(String title) {
        this(9, title);
    }

    public CraftCustomGUI() {
        this(9, "BosterParticles");
    }

    public Inventory getGUI() {
        Inventory gui = Bukkit.createInventory(null, slots, title);
        if(actions != null) {
            actions.onInventoryGeneration(gui);
        }
        return gui;
    }

    public void setTitle(String s) {
        title = s;
    }

    public void setSlots(int i) {
        slots = i;
    }

    public GUIButton getButton(int i) {
        return buttons.get(i);
    }

    public void addButton(GUIButton button) {
        buttons.put(button.getSlot(), button);
    }

    public void removeButton(int i) {
        buttons.remove(i);
    }

    public String getTitle() {
        return title;
    }

    public void setOpened(Player p) {
        onOpen(p);
        openedMap.put(p, this);
    }

    public void setClosed(Player p) {
        onClose(p);
        openedMap.remove(p);
    }

    public static CraftCustomGUI getPlayerOpenGUI(Player p) {
        return openedMap.get(p);
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

    public Collection<GUIButton> getButtons() {
        return buttons.values();
    }

    public CraftGUIActions getActions() {
        return actions;
    }

    public void setActions(CraftGUIActions actions) {
        this.actions = actions;
    }

    public boolean checkAccess(int slot) {
        if(accessibleSlots == null) return false;

        return accessibleSlots.contains(slot);
    }
}
