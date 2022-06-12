package net.boster.particles.main.particle;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.particle.blockdata.BPBlockData;
import net.boster.particles.main.particle.dust.BPDustOptions;
import net.boster.particles.main.utils.Utils;
import net.boster.particles.main.utils.log.LogType;
import net.boster.particles.main.utils.Version;
import net.boster.particles.main.particle.blockdata.BlockDataUtils;
import net.boster.particles.main.particle.dust.DustOptionsUtils;
import net.boster.particles.main.particle.provider.NewParticleProvider;
import net.boster.particles.main.particle.provider.OldParticleProvider;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class BosterParticle {

    @Getter private static final ParticleProvider provider;

    static {
        if(Version.getCurrentVersion().getVersionInteger() < 4) {
            provider = new OldParticleProvider();
        } else {
            provider = new NewParticleProvider();
        }
    }

    @Getter @Setter @NotNull private EnumBosterParticle particle;
    @Getter @Setter @NotNull private Object object;
    @Getter @Setter private int amount;
    @Getter @Setter private double options;
    @Getter @Setter @Nullable private BPDustOptions dustOptions;
    @Getter @Setter @Nullable private BPBlockData blockData;

    @Getter @Setter private double x,
            y,
            z;

    public BosterParticle(@NotNull EnumBosterParticle particle, int amount, double options, @Nullable BPDustOptions dustOptions, @Nullable BPBlockData blockData, double x, double y, double z) {
        this.particle = particle;
        this.object = provider.prepareObject(particle);
        this.amount = amount;
        this.options = options;
        this.dustOptions = dustOptions;
        this.blockData = blockData;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BosterParticle(@NotNull EnumBosterParticle particle, int amount, double options, @Nullable BPDustOptions dustOptions, @Nullable BPBlockData blockData) {
        this(particle, amount, options, dustOptions, blockData, 0, 0, 0);
    }

    public BosterParticle(@NotNull EnumBosterParticle particle, int amount, double options, @Nullable BPDustOptions dustOptions) {
        this(particle, amount, options, dustOptions, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull EnumBosterParticle particle, int amount, double options, @Nullable BPBlockData blockData) {
        this(particle, amount, options, null, blockData, 0, 0, 0);
    }

    public BosterParticle(@NotNull EnumBosterParticle particle, int amount, double options) {
        this(particle, amount, options, null, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull EnumBosterParticle particle, int amount) {
        this(particle, amount, 0, null, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull EnumBosterParticle particle) {
        this(particle, 1, 0, null, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull EnumBosterParticle particle, @Nullable BPDustOptions dustOptions) {
        this(particle, 1, 0, dustOptions, null, 0, 0, 0);
    }

    public BosterParticle(@NotNull EnumBosterParticle particle, @Nullable BPBlockData blockData) {
        this(particle, 1, 0, null, blockData, 0, 0, 0);
    }

    /**
     * This method includes catching thrown exception.
     */
    public void spawn(Location loc) {
        try {
            defaultSpawn(loc);
        } catch (Exception e) {
            BosterParticles.getInstance().log("&7Could not spawn BosterParticle \"&e" + this + "&7\". (Location = " + loc.toString() + ")", LogType.ERROR);
        }
    }

    /**
     * This method can throw an exception.
     */
    public void defaultSpawn(@NotNull Location loc) {
        if(blockData != null) {
            provider.play(object, loc, amount, x, y, z, options, blockData.get());
        } else if(dustOptions != null) {
            provider.play(object, loc, amount, x, y, z, options, dustOptions.get());
        } else {
            provider.play(object, loc, amount, x, y, z, options);
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
            EnumBosterParticle e = EnumBosterParticle.valueOf(type);
            if(e.getVersion().getVersionInteger() > Version.getCurrentVersion().getVersionInteger()) {
                BosterParticles.getInstance().log("&7Could not load Particle \"&c" + type + "&7\", because it's support starts from version " +
                        e.getVersion().name() + ", but your core version is " + Version.getCurrentVersion().getVersionInteger() + ". (Section = " + particle.getName() + ")", LogType.WARNING);
                return null;
            }

            bp = new BosterParticle(e);
        } catch (Exception e) {
            BosterParticles.getInstance().log("&7Could not load Particle \"&c" + type + "&7\". (Section = " + particle.getName() + ")", LogType.WARNING);
            return null;
        }
        bp.setAmount(particle.getInt("amount", 1));
        bp.setOptions(particle.getDouble("Options", 0));
        if(dustOptions != null && !dustOptions.equalsIgnoreCase("none")) {
            try {
                String[] ss = dustOptions.replace(" ", "").split(",");
                bp.setDustOptions(DustOptionsUtils.create(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), Integer.parseInt(ss[3])));
            } catch (Exception e) {
                BosterParticles.getInstance().log("&7Could not load DustOptions \"&c" + dustOptions + "&7\". (Section = " + particle.getName() + ")", LogType.WARNING);
                return null;
            }
        }
        String blockData = particle.getString("BlockData");
        if(blockData != null) {
            try {
                Material m = Material.valueOf(blockData);
                if(!m.isBlock()) {
                    BosterParticles.getInstance().log("&7Could not load BlockData, because input material is not a block \"&c" + blockData + "&7\". (Section = " + particle.getName() + ")", LogType.WARNING);
                } else {
                    bp.setBlockData(BlockDataUtils.createBlockData(m));
                }
            } catch (Exception e) {
                BosterParticles.getInstance().log("&7Could not load BlockData \"&c" + blockData + "&7\". (Section = " + particle.getName() + ")", LogType.WARNING);
                return null;
            }
        }
        bp.setX(particle.getDouble("X", 0));
        bp.setY(particle.getDouble("Y", 0));
        bp.setZ(particle.getDouble("Z", 0));
        return bp;
    }

    public @NotNull String serialize() {
        return Objects.requireNonNull(Utils.encode(this));
    }

    public static @NotNull BosterParticle deserialize(@NotNull String s) throws IOException, ClassNotFoundException {
        return Utils.decode(s, BosterParticle.class);
    }
}
