package net.Boster.particles.main.listeners;

import net.Boster.particles.main.BosterParticles;
import net.Boster.particles.main.data.PlayerData;
import net.Boster.particles.main.data.extensions.DeathEffects;
import net.Boster.particles.main.data.extensions.KillEffects;
import net.Boster.particles.main.gui.ParticlesGUI;
import net.Boster.particles.main.particle.CraftItemTrail;
import net.Boster.particles.main.particle.CraftTrail;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Events implements Listener {

    private final BosterParticles plugin;

    public Events(BosterParticles plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        ParticlesGUI gui = ParticlesGUI.getByCommand(e.getMessage());
        if(gui != null) {
            e.setCancelled(true);
            if(gui.getPermission() != null && !p.hasPermission(gui.getPermission())) {
                if(gui.getNoPermissionMessage() != null) {
                    p.sendMessage(BosterParticles.toColor(gui.getNoPermissionMessage()));
                }
            } else {
                gui.open(p);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerData.load(p);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogOut(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData pd = PlayerData.get(p);
        if(pd != null) {
            pd.saveData();
            pd.clear();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLaunch(ProjectileLaunchEvent e) {
        if(e.getEntity().getShooter() instanceof Player p) {
            Entity ent = e.getEntity();
            PlayerData data = PlayerData.get(p);
            if(data != null && !data.trails.isEmpty()) {
                int delay = plugin.getConfig().getInt("Setting.RunnableDelay");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(ent.isDead() || ent.isOnGround() || data.trails.isEmpty()) {
                            cancel();
                            return;
                        }

                        for(CraftTrail trail : data.trails) {
                            trail.spawn(ent.getLocation());
                        }
                    }
                }.runTaskTimer(plugin, delay, delay);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent e) {
        if(e.getItem().getCustomName() != null && e.getItem().getCustomName().equalsIgnoreCase(CraftItemTrail.NO_PICKUP)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInvPickup(InventoryPickupItemEvent e) {
        if(e.getItem().getCustomName() != null && e.getItem().getCustomName().equalsIgnoreCase(CraftItemTrail.NO_PICKUP)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerData pd = PlayerData.get(p);
        if(pd != null) {
            pd.extensions.values().stream().filter(extension -> extension instanceof DeathEffects).forEach(d -> ((DeathEffects) d).onDeath(pd));
        }
        if(p.getKiller() != null) {
            PlayerData kd = PlayerData.get(p.getKiller());
            kd.extensions.values().stream().filter(extension -> extension instanceof KillEffects).forEach(d -> ((KillEffects) d).onKill(kd, p));
        }
    }
}
