package net.boster.particles.main.listeners;

import net.boster.particles.main.gui.CraftCustomGUI;
import net.boster.particles.main.gui.GUIButton;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(e.getInventory() == null) return;

        CraftCustomGUI cg = CraftCustomGUI.getPlayerOpenGUI(p);
        if(cg != null) {
            cg.setClosed(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(e.getInventory() == null) return;
        if(e.getClickedInventory() == null) return;
        if(e.getView().getTitle() == null) return;

        CraftCustomGUI cg = CraftCustomGUI.getPlayerOpenGUI(p);
        if(cg != null) {
            if(e.getClickedInventory().getHolder() instanceof Player) {
                e.setCancelled(!cg.accessPlayerInventory);
                return;
            }

            e.setCancelled(!cg.checkAccess(e.getSlot()));
            GUIButton b = cg.getButton(e.getSlot());

            if(b != null) {
                b.onClick(p);
            }
        }
    }
}
