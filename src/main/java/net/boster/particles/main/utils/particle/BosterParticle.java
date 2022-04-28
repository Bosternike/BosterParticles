package net.boster.particles.main.utils.particle;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.LogType;
import net.boster.particles.main.utils.particle.blockdata.BlockDataUtils;
import net.boster.particles.main.utils.particle.dust.DustOptionsUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BosterParticle {

    @Getter @Setter @NotNull private Particle particle;
    @Getter @Setter private int amount;
    @Getter @Setter private int options;
    @Getter @Setter @Nullable private Object dustOptions;
    @Getter @Setter @Nullable private Object blockData;

    @Getter @Setter private double x,
            y,
            z;

    public BosterParticle(@NotNull Particle particle, int amount, int options, @Nullable Object dustOptions, @Nullable Object blockData, double x, double y, double z) {
        this.particle = particle;
        this.amount = amount;
        this.options = options;
        this.dustOptions = dustOptions;
        this.blockData = blockData;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BosterParticle(@NotNull Particle particle, int amount, int options, @Nullable Object dustOptions, @Nullable Object blockData) {
        this(particle, amount, options, dustOptions, blockData, 0, 0, 0);
    }

    public BosterParticle(@NotNull Particle particle, int amount, int options, @Nullable Object dustOptions) {
        this(particle, amount, options, dustOptions, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull Particle particle, int amount, int options) {
        this(particle, amount, options, null, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull Particle particle, int amount) {
        this(particle, amount, 0, null, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull Particle particle) {
        this(particle, 1, 0, null, null, 0, 0, 0);
    }

    public void spawn(Location loc) {
        try {
            defaultSpawn(loc);
        } catch (Exception e) {
            BosterParticles.getInstance().log("&7Could not spawn BosterParticle \"&e" + this + "&7\". (Location = " + loc.toString() + ")", LogType.ERROR);
        }
    }

    public void defaultSpawn(Location loc) {
        if(blockData != null) {
            loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, options, blockData);
        } else if(dustOptions != null) {
            loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, options, dustOptions);
        } else {
            loc.getWorld().spawnParticle(particle, loc, amount, x, y, z, options);
        }
    }

    public String toString() {
        return "[Particle = " + particle.name() + ", amount = " + amount + ", options = " + options + ", DustOptions" +
                (dustOptions != null ? dustOptions.toString() : "null") + ", BlockData = " + (blockData != null ? blockData.toString() : "null") +
                ", x = " + x + ", y = " + y + ", z = " + z + "]";
    }

    public static BosterParticle load(ConfigurationSection particle) {
        if(particle == null) return null;

        String type = particle.getString("Type");
        String dustOptions = particle.getString("DustOptions");
        BosterParticle bp;
        try {
            bp = new BosterParticle(Particle.valueOf(type));
        } catch (Exception e) {
            BosterParticles.getInstance().log("&7Could not load Particle \"&c" + type + "&7\". (Particle = " + particle.getName() + ")", LogType.WARNING);
            return null;
        }
        bp.setAmount(particle.getInt("amount", 1));
        bp.setOptions(particle.getInt("Options", 0));
        if(dustOptions != null && !dustOptions.equalsIgnoreCase("none")) {
            try {
                String[] ss = dustOptions.replace(" ", "").split(",");
                bp.setDustOptions(DustOptionsUtils.create(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), Integer.parseInt(ss[3])));
            } catch (Exception e) {
                BosterParticles.getInstance().log("&7Could not load DustOptions \"&c" + dustOptions + "&7\". (Particle = " + particle.getName() + ")", LogType.WARNING);
                return null;
            }
        }
        String blockData = particle.getString("BlockData");
        if(blockData != null) {
            try {
                Material m = Material.valueOf(blockData);
                if(!m.isBlock()) {
                    BosterParticles.getInstance().log("&7Could not load BlockData, because input material is not a block \"&c" + blockData + "&7\". (Particle = " + particle.getName() + ")", LogType.WARNING);
                } else {
                    bp.setBlockData(BlockDataUtils.createBlockData(m));
                }
            } catch (Exception e) {
                BosterParticles.getInstance().log("&7Could not load BlockData \"&c" + blockData + "&7\". (Particle = " + particle.getName() + ")", LogType.WARNING);
                return null;
            }
        }
        bp.setX(particle.getInt("X", 0));
        bp.setY(particle.getInt("Y", 0));
        bp.setZ(particle.getInt("Z", 0));
        return bp;
    }
}
