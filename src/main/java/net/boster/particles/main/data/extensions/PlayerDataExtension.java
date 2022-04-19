package net.boster.particles.main.data.extensions;

import lombok.Getter;
import net.boster.particles.main.data.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerDataExtension {

    @Getter @NotNull private final String id;
    @Getter protected Player player;
    @Getter protected PlayerData data;

    public PlayerDataExtension(@NotNull String id, @NotNull PlayerData data) {
        this.id = id;
        this.data = data;
        this.player = data.p;
    }

    /**
     * You don't need to manually save the data.
     * This method must be saving data to FileConfiguration
     * from {@link #data} - {@link PlayerData}
     */
    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    public void saveData() {

    }

    /**
     * This method will be performed when {@link #data} is cleared.
     */
    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    public void onClear() {

    }

    /**
     * This method will be performed when configuration section is cleared throw {@link #data}.
     * All this plugin's methods clear sections throw {@link #data}.
     * @param section configuration section name.
     */
    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    public void onSectionCleared(String section) {

    }

    /**
     * This method will be performed when file is cleared throw {@link #data}.
     * All this plugin's methods clear file throw {@link #data}.
     */
    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    public void onFileCleared() {

    }

    /**
     * This method will be performed when the {@link #data} is updated.
     */
    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    public void onUpdate() {

    }
}
