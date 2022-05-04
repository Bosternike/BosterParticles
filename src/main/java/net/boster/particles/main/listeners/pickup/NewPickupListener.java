package net.boster.particles.main.listeners.pickup;

import net.boster.particles.main.trail.CraftItemTrail;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class NewPickupListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent e) {
        if(e.getItem().getCustomName() != null && e.getItem().getCustomName().equalsIgnoreCase(CraftItemTrail.NO_PICKUP)) {
            e.setCancelled(true);
        }
    }
}
