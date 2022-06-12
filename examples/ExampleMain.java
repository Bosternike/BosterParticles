import net.boster.particles.api.BosterParticlesAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleMain extends JavaPlugin {

    public void onEnable() {
        BosterParticlesAPI.registerPlayerDataExtension("example_extension", ExampleExtension.class);
        BosterParticlesAPI.registerTrailsExtension(ExampleTrailExtension.class);
    }
}
