package net.boster.particles.main.utils;

import net.boster.particles.main.BosterParticles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BosterParticle {

    private @NotNull Particle particle;
    private int amount;
    private int options;
    private @Nullable Particle.DustOptions dustOptions;
    private @Nullable BlockData blockData;

    private double x,
            y,
            z;

    public BosterParticle(@NotNull Particle particle, int amount, int options, @Nullable Particle.DustOptions dustOptions, @Nullable BlockData blockData, double x, double y, double z) {
        this.particle = particle;
        this.amount = amount;
        this.options = options;
        this.dustOptions = dustOptions;
        this.blockData = blockData;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BosterParticle(@NotNull Particle particle, int amount, int options, @Nullable Particle.DustOptions dustOptions, @Nullable BlockData blockData) {
        this(particle, amount, options, dustOptions, blockData, 0, 0, 0);
    }

    public BosterParticle(@NotNull Particle particle, int amount, int options, @Nullable Particle.DustOptions dustOptions) {
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

    public @NotNull Particle getParticle() {
        return particle;
    }

    public void setParticle(@NotNull Particle particle) {
        this.particle = particle;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getOptions() {
        return options;
    }

    public void setOptions(int options) {
        this.options = options;
    }

    public @Nullable Particle.DustOptions getDustOptions() {
        return dustOptions;
    }

    public void setDustOptions(@Nullable Particle.DustOptions dustOptions) {
        this.dustOptions = dustOptions;
    }

    public @Nullable BlockData getBlockData() {
        return blockData;
    }

    public void setBlockData(@Nullable BlockData blockData) {
        this.blockData = blockData;
    }

    public double getX() {
        return x;
    }

    public void setX(double i) {
        x = i;
    }

    public double getY() {
        return y;
    }

    public void setY(double i) {
        y = i;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double i) {
        z = i;
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
                Color c = Color.fromBGR(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
                bp.setDustOptions(new Particle.DustOptions(c, Integer.parseInt(ss[3])));
            } catch (Exception e) {
                BosterParticles.getInstance().log("&7Could not load DustOptions \"&c" + dustOptions + "&7\". (Particle = " + particle.getName() + ")", LogType.WARNING);
                return null;
            }
        }
        String blockData = particle.getString("BlockData");
        if(blockData != null) {
            try {
                bp.setBlockData(Material.valueOf(blockData).createBlockData());
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
