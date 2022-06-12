package net.boster.particles.api.extension;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BPExtension {

    @NotNull Plugin getPlugin();

    default @NotNull String getVersion() {
        return getPlugin().getDescription().getVersion();
    }

    default @NotNull List<String> getAuthors() {
        return getPlugin().getDescription().getAuthors();
    }
}
