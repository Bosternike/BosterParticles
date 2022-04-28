package net.boster.particles.main.gui.manage.chat;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class TypingUser implements ChatInputObserver {

    public static final String[] MESSAGE = new String[]{"&f", "%prefix% &fYou are in &6typing &fmode now.",
            "%prefix% &fType &c&l/cancel &fto leave typing mode."};

    public static final Map<Player, TypingUser> user = new HashMap<>();

    @Getter @NotNull private final Player player;

    public TypingUser(@NotNull Player p) {
        user.put(p, this);
        this.player = p;
    }

    public void clear() {
        user.remove(player);
    }
}
