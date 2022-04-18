package net.boster.particles.main.particle.playertrail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.boster.particles.main.particle.playertrail.types.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;

@RequiredArgsConstructor
public abstract class PlayerTrailType {

    @Getter @NotNull protected final String id;

    private static final HashMap<String, PlayerTrailType> map = new HashMap<>();

    public static final CirculatingRing CIRCULATING_RING = new CirculatingRing();
    public static final MonoRing MONO_RING = new MonoRing();
    public static final UpTrail UP_TRAIL = new UpTrail();
    public static final MiddleTrail MIDDLE_TRAIL = new MiddleTrail();
    public static DownTrail DOWN_TRAIL = new DownTrail();

    public abstract void run(@NotNull CraftPlayerTrail trail);

    public final void register() {
        map.put(id, this);
    }

    public final void unregister() {
        map.remove(id);
    }

    public static @Nullable PlayerTrailType valueOf(@NotNull String s) {
        return map.get(s);
    }

    public static Collection<PlayerTrailType> types() {
        return map.values();
    }
}
