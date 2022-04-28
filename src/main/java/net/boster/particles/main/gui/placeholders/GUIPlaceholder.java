package net.boster.particles.main.gui.placeholders;

import org.apache.commons.lang.Validate;

public class GUIPlaceholder {

    public final String notNull;
    public String nullable;

    public GUIPlaceholder(String notNull) {
        Validate.notNull(notNull);
        this.notNull = notNull;
    }

    public String get() {
        if(nullable == null) {
            return notNull;
        } else {
            return nullable;
        }
    }
}
