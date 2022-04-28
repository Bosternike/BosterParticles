package net.boster.particles.main.listeners;

import net.boster.particles.main.gui.CustomGUI;
import net.boster.particles.main.gui.manage.confirmation.ConfirmationGUI;
import net.boster.particles.main.gui.multipage.MultiPageGUI;
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

        ConfirmationGUI confirm = ConfirmationGUI.map.get(p);
        if(confirm != null) {
            confirm.clear();
            if(!confirm.isClosed()) {
                confirm.close();
            }
            return;
        }

        MultiPageGUI mpg = MultiPageGUI.get(p);
        if(mpg != null && !mpg.isClosed()) {
            mpg.onInventoryClosed(e);
            return;
        }

        CustomGUI cg = CustomGUI.get(p);
        if(cg != null) {
            cg.onClose(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(e.getInventory() == null) return;
        if(e.getClickedInventory() == null) return;

        ConfirmationGUI confirm = ConfirmationGUI.map.get(p);
        if(confirm != null) {
            if(e.getSlot() == confirm.getAcceptSlot()) {
                confirm.onAccept();
            } else if(e.getSlot() == confirm.getDenySlot()) {
                confirm.onDeny();
            }
            e.setCancelled(true);
            return;
        }

        MultiPageGUI mpg = MultiPageGUI.get(p);
        if(mpg != null && !mpg.isClosed()) {
            mpg.onClick(e);
            return;
        }

        CustomGUI cg = CustomGUI.get(p);
        if(cg != null) {
            cg.onClick(e);
        }
    }
}
