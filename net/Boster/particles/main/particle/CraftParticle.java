package net.Boster.particles.main.particle;

import net.Boster.particles.main.BosterParticles;
import net.Boster.particles.main.data.PlayerData;
import net.Boster.particles.main.utils.LogType;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CraftParticle extends CraftTrail {

    public final Player p;
    public final String player;
    public Particle particle;
    public int amount;
    public int options;
    public Particle.DustOptions dustOptions;
    public BlockData blockData;

    public int x = 0, y = 0, z = 0;

    public CraftParticle(Player p, Particle particle, int amount, Particle.DustOptions dustOptions) {
        this.particle = particle;
        this.amount = amount;
        this.dustOptions = dustOptions;
        this.p = p;
        this.player = p.getName();
    }

    public CraftParticle(Player p, Particle particle, int amount) {
        this(p, particle, amount, null);
    }

    public CraftParticle(Player p, Particle particle) {
        this(p, particle, 1);
    }

    public CraftParticle(Player p) {
        this.p = p;
        this.player = p.getName();
    }

    public static void load(PlayerData data) {
        Player p = data.p;
        FileConfiguration file = data.data;
        ConfigurationSection particles = file.getConfigurationSection("Particles");
        if(particles != null && particles.getKeys(false).size() > 0) {
            for(String pp : particles.getKeys(false)) {
                boolean load = true;
                ConfigurationSection particle = particles.getConfigurationSection(pp);
                String type = particle.getString("Type");
                String dustOptions = particle.getString("DustOptions");
                CraftParticle cp = new CraftParticle(p);
                try {
                    cp.particle = Particle.valueOf(type);
                } catch (Exception e) {
                    load = false;
                    BosterParticles.getInstance().log("&7Could not load Particle \"&c" + type + "&7\". (Particle = " + particle.getName() + "; Player = " + p.getName() + ")", LogType.WARNING);
                }
                cp.amount = particle.getInt("amount", 1);
                cp.options = particle.getInt("Options", 0);
                if(dustOptions != null && !dustOptions.equalsIgnoreCase("none")) {
                    try {
                        String[] ss = dustOptions.replace(" ", "").split(",");
                        Color c = Color.fromBGR(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
                        cp.dustOptions = new Particle.DustOptions(c, Integer.parseInt(ss[3]));
                    } catch (Exception e) {
                        load = false;
                        BosterParticles.getInstance().log("&7Could not load DustOptions \"&c" + dustOptions + "&7\". (Particle = " + particle.getName() + "; Player = " + p.getName() + ")", LogType.WARNING);
                    }
                }
                String blockData = particle.getString("BlockData");
                if(blockData != null) {
                    try {
                        cp.blockData = Material.valueOf(blockData).createBlockData();
                    } catch (Exception e) {
                        BosterParticles.getInstance().log("&7Could not load BlockData \"&c" + blockData + "&7\". (Particle = " + particle.getName() + "; Player = " + p.getName() + ")", LogType.WARNING);
                    }
                }
                cp.x = particle.getInt("X", 0);
                cp.y = particle.getInt("Y", 0);
                cp.z = particle.getInt("Z", 0);
                if(load) {
                    data.trails.add(cp);
                }
            }
        }
    }

    @Override
    public void spawn(Location loc) {
        if(particle == null) {
            BosterParticles.getInstance().log("&7Could not spawn Particle because it's null. (Location = " + loc.toString() + "; Player = " + player + ")", LogType.ERROR);
            return;
        }

        try {
            if(blockData != null) {
                loc.getWorld().spawnParticle(particle, loc, amount, 0,0, 0, options, blockData);
            } else if(dustOptions != null) {
                loc.getWorld().spawnParticle(particle, loc, amount, 0,0, 0, options, dustOptions);
            } else {
                loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, options);
            }
        } catch (Exception e) {
            String op = dustOptions != null ? dustOptions.toString() : "null";
            BosterParticles.getInstance().log("&7Could not spawn Particle \"&e" + particle.name() + "&7\" with amount &6" + amount + " &7and DustOptions &6" + op + "&7. (Location = " + loc.toString() + "; Player = " + player + ")", LogType.ERROR);
        }
    }
}
