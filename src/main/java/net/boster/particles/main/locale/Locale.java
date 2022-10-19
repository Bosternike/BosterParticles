package net.boster.particles.main.locale;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Locale {

    @NotNull private final String id;
    @NotNull private final List<String> aliases;
    @Nullable private File file;
    @NotNull private final FileConfiguration configuration;
}
