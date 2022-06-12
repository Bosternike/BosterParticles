package net.boster.particles.main.listeners.merge;

import net.boster.particles.main.nms.NMSProvider;
import net.boster.particles.main.trail.CraftItemTrail;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;

public class ItemMergeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemMerge(ItemMergeEvent e) {
        if(e.getEntity().getCustomName() != null) {
            if(e.getEntity().getCustomName().equalsIgnoreCase(CraftItemTrail.NO_PICKUP) || e.getEntity().getCustomName().equalsIgnoreCase(NMSProvider.NO_MERGE)) {
                e.setCancelled(true);
            }
        }
    }
}
