package net.boster.particles.main.gui;

import lombok.Getter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.gui.button.GUIButton;
import net.boster.particles.main.gui.craft.CraftCustomGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CustomGUI {

    public static final HashMap<Player, CustomGUI> openedMap = new HashMap<>();

    @Getter @NotNull private final Player player;
    @Getter @NotNull private Inventory inventory;
    @Getter @NotNull private final CraftCustomGUI gui;

    private BukkitTask closedTask;
    @Getter private boolean closed = false;

    public CustomGUI(@NotNull Player p, @NotNull CraftCustomGUI gui) {
        openedMap.put(p, this);
        this.player = p;
        this.gui = gui;
        this.inventory = gui.getGUI();
    }

    public CustomGUI(@NotNull Player p, @NotNull String title, int size) {
        this(p, new CraftCustomGUI(size, title));
    }

    public CustomGUI(@NotNull Player p, @NotNull String title) {
        this(p, new CraftCustomGUI(title));
    }

    public CustomGUI(@NotNull Player p, int size) {
        this(p, new CraftCustomGUI(size));
    }

    public CustomGUI(@NotNull Player p) {
        this(p, new CraftCustomGUI());
    }

    public static CustomGUI get(Player p) {
        return openedMap.get(p);
    }

    public void open() {
        open(gui.getTitle());
    }

    public void open(String title) {
        setClosed(1);
        inventory = gui.getGUI(title);
        fillGUI();
        player.openInventory(inventory);
        gui.onOpen(player);
    }

    public void fillGUI() {
        for(GUIButton i : gui.getButtons()) {
            inventory.setItem(i.getSlot(), i.prepareItem(player));
        }
    }

    public void setClosed(boolean b) {
        closed = b;
    }

    public void setClosed(int ticks) {
        if(closedTask != null) {
            closedTask.cancel();
        }

        Bukkit.getScheduler().runTaskLater(BosterParticles.getInstance(), () -> {
            closed = false;
            closedTask = null;
            if(!player.isOnline()) {
                clear();
            }
        }, ticks);
    }

    public void onClick(InventoryClickEvent e) {
        if(closed) return;

        Player p = (Player) e.getWhoClicked();

        if(e.getClickedInventory().getHolder() instanceof Player) {
            e.setCancelled(!gui.accessPlayerInventory);
            return;
        }

        e.setCancelled(!gui.checkAccess(e.getSlot()));
        GUIButton b = gui.getButton(e.getSlot());

        if(b != null) {
            if(e.isRightClick()) {
                b.onRightClick(p);
            } else {
                b.onLeftClick(p);
            }
        }
    }

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        Bukkit.getScheduler().runTaskLater(BosterParticles.getInstance(), () -> {
            if(!closed && inventory != p.getOpenInventory().getTopInventory()) {
                gui.onClose(p);
                clear();
            }
        }, 1);
    }

    public void clear() {
        openedMap.remove(player);
    }
}
