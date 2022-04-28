package net.boster.particles.main.particle;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.utils.LogType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftItemTrail extends CraftTrail {

    public static final String NO_PICKUP = "§bBosterParticles §eitem, that can't be picked up!";

    public final Player p;
    public final String player;
    public final ItemStack item;
    public final int lifeTime;
    public int amount;
    public boolean pickupAble;

    public CraftItemTrail(Player p, ItemStack item, int amount, int lifeTime, boolean pickupAble) {
        this.item = item;
        this.amount = amount;
        this.lifeTime = lifeTime;
        this.pickupAble = pickupAble;
        this.p = p;
        this.player = p.getName();
    }

    public CraftItemTrail(Player p, ItemStack item) {
        this(p, item, 1, 40, false);
    }

    public static void load(PlayerData data) {
        Player p = data.p;
        FileConfiguration file = data.data;
        ConfigurationSection items = file.getConfigurationSection("Items");
        if(items != null && items.getKeys(false).size() > 0) {
            for(String i : items.getKeys(false)) {
                ConfigurationSection item = items.getConfigurationSection(i);

                Material material = null;
                try {
                    material = Material.valueOf(item.getString("Material"));
                } catch (Exception e) {
                    BosterParticles.getInstance().log("&7Could not load Material \"&c" + item.getString("Material") + "&7\". (Item = " + item.getName() + "; Player = " + p.getName() + ")", LogType.WARNING);
                }

                if(material != null) {
                    data.trails.add(new CraftItemTrail(p, new ItemStack(material), item.getInt("Amount", 1), item.getInt("LifeTime", 40), item.getBoolean("PickupAble", false)));
                }
            }
        }
    }

    @Override
    public void spawn(@NotNull Location loc) {
        if(item == null) {
            BosterParticles.getInstance().log("&7Could not spawn Item because it's null. (Location = " + loc.toString() + "; Player = " + player + ")", LogType.ERROR);
            return;
        }

        for(int i = 0; i < amount; i++) {
            Entity ent = loc.getWorld().dropItemNaturally(loc, item);
            if(!pickupAble) {
                ent.setCustomNameVisible(false);
                ent.setCustomName(NO_PICKUP);
            }

            if(lifeTime > -1) {
                BosterParticles.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(BosterParticles.getInstance(), ent::remove, lifeTime);
            }
        }
    }
}
