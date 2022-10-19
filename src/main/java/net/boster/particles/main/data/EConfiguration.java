package net.boster.particles.main.data;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EConfiguration extends YamlConfiguration {

    @Getter private boolean edited = false;

    public void set(@NotNull String path, @Nullable Object object) {
        super.set(path, object);
        edited = true;
    }
}
