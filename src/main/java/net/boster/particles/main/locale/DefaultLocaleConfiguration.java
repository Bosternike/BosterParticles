package net.boster.particles.main.locale;

import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultLocaleConfiguration extends YamlConfiguration {

    public DefaultLocaleConfiguration() {
        set("Messages.noPermission", "%prefix% &cYou don't have permission to use this command!");
        set("Messages.reload", "%prefix% &fThe plugin has been reloaded!");
        set("Messages.help", "%prefix% &fReload the plugin &7- &d/bosterparticles reload");

        set("Messages.list.noSuchHelp", "%prefix% &fHelp page &c%name% &fcould not be found.");

        set("Messages.set.usage", "%prefix% &fUsage &7- &d/bosterparticles set [user] [trail]");
        set("Messages.set.nullTrail", "%prefix% &fTrail &c%name% &fdoes not exist.");
        set("Messages.set.success", "%prefix% &fTrail &a%name% &fwas set for player &b%player%&f!");

        set("Messages.open.nullPlayer", "%prefix% &fPlayer &c%name% &fcould not be found.");
        set("Messages.open.success", "%prefix% &a%gui% &fwas opened for player &d%player%&f!");
    }
}
