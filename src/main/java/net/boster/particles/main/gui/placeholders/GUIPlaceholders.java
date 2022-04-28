package net.boster.particles.main.gui.placeholders;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GUIPlaceholders {

    private final HashMap<String, GUIPlaceholder> placeholders = new HashMap<>();

    public String getPlaceholder(String s) {
        return placeholders.get(s).get();
    }

    public GUIPlaceholder getGUIPlaceholder(String s) {
        return placeholders.get(s);
    }

    public void setPlaceholder(String placeholder, String notNull, String nullable) {
        GUIPlaceholder pp = new GUIPlaceholder(notNull);
        pp.nullable = nullable;
        placeholders.put(placeholder, pp);
    }

    public void setGUIPlaceholder(String placeholder, GUIPlaceholder gp) {
        placeholders.put(placeholder, gp);
    }

    public Set<Map.Entry<String, GUIPlaceholder>> placeholders() {
        return placeholders.entrySet();
    }

    public void clear() {
        placeholders.clear();
    }
}
