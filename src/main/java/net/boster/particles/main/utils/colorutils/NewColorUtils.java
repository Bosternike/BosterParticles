package net.boster.particles.main.utils.colorutils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewColorUtils implements ColorUtils {

    private final Pattern pattern;

    public NewColorUtils() {
        this.pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
    }

    @Override
    public String toColor(@NotNull String s) {
        String str = s;
        for(Matcher matcher = pattern.matcher(str); matcher.find(); matcher = pattern.matcher(str)) {
            String color = str.substring(matcher.start() + 1, matcher.end());
            str = str.replace("&" + color, ChatColor.of(color) + "");
        }

        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
