package net.boster.particles.main.particle.playertrail;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.extensions.PlayerTrailExtension;
import net.boster.particles.main.particle.playertrail.types.limiter.DistanceLimiter;
import net.boster.particles.main.particle.playertrail.types.limiter.HistoryLimiter;
import net.boster.particles.main.particle.playertrail.types.limiter.LocationsLimiter;
import net.boster.particles.main.utils.particle.BosterParticle;
import net.boster.particles.main.utils.LogType;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CraftPlayerTrail {

    @Getter @Setter @NotNull private List<Location> history = new ArrayList<>();

    public final @NotNull PlayerTrailExtension data;
    public final @NotNull Player p;
    public final @NotNull BosterParticle particle;
    public final @NotNull PlayerTrailType type;
    public int delay = 0;
    public double radius = 0.5;
    public @Nullable HistoryLimiter limiter;
    public int historyShowDelay;
    public boolean stopOnMove;

    public CraftPlayerTrail(@NotNull PlayerTrailExtension data, @NotNull BosterParticle particle, @NotNull PlayerTrailType type) {
        this.data = data;
        this.p = data.getPlayer();
        this.particle = particle;
        this.type = type;
    }

    public static void load(@NotNull PlayerTrailExtension data) {
        FileConfiguration file = data.getData().data;
        ConfigurationSection particles = file.getConfigurationSection("PlayerTrails");
        if(particles != null && particles.getKeys(false).size() > 0) {
            for(String pp : particles.getKeys(false)) {
                ConfigurationSection section = particles.getConfigurationSection(pp);
                BosterParticle bp = BosterParticle.load(section);
                if(bp != null) {
                    HistoryLimiter limiter = null;
                    if(section.get("Limiter.Distance") != null) {
                        limiter = new DistanceLimiter(section.getDouble("Limiter.Distance"));
                    } else if(section.get("Limiter.Locations") != null) {
                        limiter = new LocationsLimiter(section.getInt("Limiter.Locations"));
                    }

                    PlayerTrailType type = PlayerTrailType.valueOf(section.getString("Figure"));
                    if(type == null) {
                        BosterParticles.getInstance().log("&7Could not load Figure \"&c" + section.getString("Figure") + "&7\"", LogType.WARNING);
                        return;
                    }

                    if(type.getId().contains("HISTORY") && limiter == null) {
                        BosterParticles.getInstance().log("&7Figure \"&c" + type.getId() + "&7\" requires limiter!", LogType.WARNING);
                        return;
                    }

                    CraftPlayerTrail cp = new CraftPlayerTrail(data, bp, type);
                    cp.delay = section.getInt("Delay", 0);
                    cp.radius = section.getDouble("Radius", 0.5);
                    cp.limiter = limiter;
                    cp.historyShowDelay = section.getInt("HistoryShowDelay", 7);
                    cp.stopOnMove = section.getBoolean("StopOnMove", false);
                    data.trails.add(cp);
                }
            }
        }
    }

    public void start() {
        type.run(this);
    }
}
