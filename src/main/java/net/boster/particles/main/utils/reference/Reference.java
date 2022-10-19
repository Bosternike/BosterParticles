package net.boster.particles.main.utils.reference;

import org.jetbrains.annotations.NotNull;

public interface Reference<T, E> {

    T get(@NotNull E e);
}
