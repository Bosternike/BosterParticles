package net.boster.particles.main.commands;

import com.google.common.collect.Lists;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.data.extensions.PlayerDataExtension;
import net.boster.particles.main.gui.manage.MenusListGUI;
import net.boster.particles.main.trail.CraftTrail;
import net.boster.particles.main.utils.CustomTrailsUtils;
import net.boster.particles.main.utils.Utils;
import net.boster.particles.main.utils.Version;
import net.boster.particles.main.particle.EnumBosterParticle;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Commands extends BosterCommand {

    private final List<String> subCommands = Lists.newArrayList("reload", "set", "menus", "list");

    public Commands(BosterParticles plugin) {
        super(plugin, "bosterparticles", "bostertrails");
        register();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("bosterparticles.commands.admin")) {
            sender.sendMessage(BosterParticles.toColor(getLocaleMessage(sender, "Messages.noPermission")));
            return false;
        }

        if(args.length == 0) {
            sendHelp(sender);
            return false;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            plugin.getLoader().reload();
            sender.sendMessage(BosterParticles.toColor(getLocaleMessage(sender, "Messages.reload")));
            return true;
        } else if(args[0].equalsIgnoreCase("menus")) {
            if(!checkPlayer(sender)) return false;

            MenusListGUI.open((Player) sender);
            return true;
        } else if(args[0].equalsIgnoreCase("set")) {
            if(args.length < 3) {
                sender.sendMessage(BosterParticles.toColor(getLocaleMessage(sender, "Messages.set.usage")));
                return true;
            }

            OfflinePlayer p;
            if(DataConverter.getDataConverterType() == DataConverter.DataConverterType.NAME) {
                p = Bukkit.getOfflinePlayer(args[1]);
            } else {
                try {
                    p = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));
                } catch (Exception e) {
                    p = Bukkit.getOfflinePlayer(args[1]);
                }
            }

            CustomTrailsUtils.CustomTrail t = CustomTrailsUtils.get(args[2]);
            if(t == null) {
                sender.sendMessage(BosterParticles.toColor(getLocaleMessage(sender, "Messages.set.nullTrail").replace("%name%", args[2])));
                return true;
            }

            t.act(p);
            sender.sendMessage(BosterParticles.toColor(getLocaleMessage(sender, "Messages.set.success").replace("%name%", args[2]).replace("%player%", args[1])));
            return true;
        } else if(args[0].equalsIgnoreCase("list")) {
            if(args.length < 2) {
                sender.sendMessage(Utils.toColor(getLocaleMessage(sender, "Messages.list.noSuchHelp").replace("%name%", "null")));
                return false;
            }

            if(plugin.getConfig().getConfigurationSection("Messages.list." + args[1]) != null) {
                sendList(sender, args[1]);
            } else {
                sender.sendMessage(Utils.toColor(getLocaleMessage(sender, "Messages.list.noSuchHelp").replace("%name%", args[1])));
            }
            return true;
        } else {
            sendHelp(sender);
            return false;
        }
    }

    public void sendHelp(CommandSender sender) {
        for(String s : getLocaleMessageList(sender, "Messages.help")) {
            sender.sendMessage(BosterParticles.toColor(s));
        }
    }

    public void sendList(CommandSender sender, String path) {
        String dot = getLocaleMessage(sender, "Messages.list." + path + ".dot");
        String dt = "";
        String classes = "";
        for(Class<? extends CraftTrail> t : CraftTrail.registration) {
            classes += dt + t.getSimpleName();
            dt = dot;
        }
        dt = "";
        String extensions = "";
        for(Class<? extends PlayerDataExtension> t : PlayerData.getRegisteredExtensions()) {
            extensions += dt + t.getSimpleName();
            dt = dot;
        }
        dt = "";
        String particles = "";
        for(EnumBosterParticle particle : EnumBosterParticle.values()) {
            if(particle.getVersion().getVersionInteger() <= Version.getCurrentVersion().getVersionInteger()) {
                particles += dt + particle.name();
                dt = dot;
            }
        }
        for(String s : getLocaleMessageList(sender, "Messages.list." + path + ".format")) {
            sender.sendMessage(BosterParticles.toColor(s.replace("%classes%", classes)
                    .replace("%extensions%", extensions).replace("%particles%", particles)));
        }
    }

    @Override
    @NotNull
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(args.length < 2) {
            return createDefaultTabComplete(subCommands, args, 0);
        } else {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }
    }
}
