package net.boster.particles.main.data.database.setter;

import net.boster.particles.main.data.EConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DataSetter {

    @NotNull String getName();

    void setUserData(@NotNull String uuid, @NotNull String w, @Nullable String o);

    String getUserData(@NotNull String uuid, @NotNull String value);

    @NotNull EConfiguration configuration(@NotNull String uuid);

    void save(@NotNull String uuid, @NotNull EConfiguration file);

    void deleteUser(@NotNull String uuid);
}
