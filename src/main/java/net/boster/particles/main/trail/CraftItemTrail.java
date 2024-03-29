package net.boster.particles.main.trail;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.nms.NMSProvider;
import net.boster.particles.main.utils.Utils;
import net.boster.particles.main.utils.log.LogType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
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

    public CraftItemTrail(@NotNull Player p, @NotNull ItemStack item, int amount, int lifeTime, boolean pickupAble) {
        this.item = item;
        this.amount = amount;
        this.lifeTime = lifeTime;
        this.pickupAble = pickupAble;
        this.p = p;
        this.player = p.getName();
    }

    public CraftItemTrail(@NotNull Player p, @NotNull ItemStack item) {
        this(p, item, 1, 40, false);
    }

    public static void load(@NotNull PlayerData data) {
        Player p = data.getPlayer();
        FileConfiguration file = data.data;
        ConfigurationSection items = file.getConfigurationSection("Items");
        if(items != null && items.getKeys(false).size() > 0) {
            for(String i : items.getKeys(false)) {
                ConfigurationSection item = items.getConfigurationSection(i);

                ItemStack itemStack = null;
                ConfigurationSection itemSection = item.getConfigurationSection("ItemStack");
                if(itemSection != null) {
                    itemStack = Utils.getItemStack(itemSection);
                    if(itemStack == null) {
                        BosterParticles.getInstance().log("&7Could not load ItemStack. (Item = " + item.getName() + "; Player = " + p.getName() + ")", LogType.WARNING);
                    }
                } else {
                    try {
                        itemStack = new ItemStack(Material.valueOf(item.getString("Material")));
                    } catch (Exception e) {
                        BosterParticles.getInstance().log("&7Could not load Material \"&c" + item.getString("Material") + "&7\". (Item = " + item.getName() + "; Player = " + p.getName() + ")", LogType.WARNING);
                    }
                }

                if(itemStack != null) {
                    data.trails.add(new CraftItemTrail(p, itemStack, item.getInt("Amount", 1), item.getInt("LifeTime", 40), item.getBoolean("PickupAble", false)));
                }
            }
        }
    }

    @Override
    public void spawn(@NotNull Location loc) {
        if(item == null) {
            BosterParticles.getInstance().log("&7Could not spawn Item because it's null. (Location = " + loc + "; Player = " + player + ")", LogType.ERROR);
            return;
        }

        for(int i = 0; i < amount; i++) {
            Item ent = NMSProvider.createItem(loc, item);
            ent.setCustomNameVisible(false);
            if(!pickupAble) {
                ent.setCustomName(NO_PICKUP);
            }

            if(lifeTime > -1) {
                BosterParticles.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(BosterParticles.getInstance(), ent::remove, lifeTime);
            }
        }
    }
}
