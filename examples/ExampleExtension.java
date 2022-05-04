import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.extensions.DeathEffects;
import net.boster.particles.main.data.extensions.KillEffects;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.particle.BosterParticle;
import net.boster.particles.main.particle.EnumBosterParticle;
import net.boster.particles.main.particle.ParticleUtils;
import net.boster.particles.main.particle.dust.DustOptionsUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ExampleExtension extends PlayerDataExtension implements DeathEffects, KillEffects {

    private final boolean hasDeathEffect;
    private final boolean hasKillEffect;

    public ExampleExtension(@NotNull String id, @NotNull PlayerData data) {
        super(id, data);
        this.hasDeathEffect = data.data.getBoolean("hasDeathEffect", false);
        this.hasKillEffect = data.data.getBoolean("hasKillEffect", false);
    }

    @Override
    public void onDeath(@NotNull PlayerData data) {
        if(hasDeathEffect) {
            BosterParticle particle = new BosterParticle(EnumBosterParticle.REDSTONE, DustOptionsUtils.create(252, 14, 197, 1));
            ParticleUtils.playCircularEffect(data.p.getLocation(), particle);
        }
    }

    @Override
    public void onKill(@NotNull PlayerData killer, @NotNull Player killed) {
        if(hasKillEffect) {
            ParticleUtils.runDampenedRadialWaves(killed.getLocation());
        }
    }
}
