package examples;

import net.Boster.particles.main.BosterParticles;
import org.bukkit.plugin.java.JavaPlugin;

public class ExampleMain extends JavaPlugin {

    public void onEnable() {
        BosterParticles.registerPlayerDataExtension("example_extension", ExampleExtension.class);
        BosterParticles.registerTrailsExtension(ExampleTrailExtension.class);
    }
}
