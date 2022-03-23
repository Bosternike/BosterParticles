package net.boster.particles.main.commands;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.database.DataConverter;
import net.boster.particles.main.particle.CraftTrail;
import net.boster.particles.main.utils.CustomTrails;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class Commands implements CommandExecutor {

    private final BosterParticles plugin;

    public Commands(BosterParticles plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("bosterparticles.commands.admin")) {
            sender.sendMessage(BosterParticles.toColor(plugin.getConfig().getString("Messages.noPermission")));
            return false;
        }

        if(args.length == 0) {
            sendHelp(sender);
            return false;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            plugin.getLoader().reload();
            sender.sendMessage(BosterParticles.toColor(plugin.getConfig().getString("Messages.reload")));
            return true;
        } else if(args[0].equalsIgnoreCase("set")) {
            if(args.length < 3) {
                sender.sendMessage(BosterParticles.toColor(plugin.getConfig().getString("Messages.set.usage")));
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

            CustomTrails.CustomTrail t = CustomTrails.get(args[2]);
            if(t == null) {
                sender.sendMessage(BosterParticles.toColor(plugin.getConfig().getString("Messages.set.nullTrail").replace("%name%", args[2])));
                return true;
            }

            t.act(p);
            sender.sendMessage(BosterParticles.toColor(plugin.getConfig().getString("Messages.set.success").replace("%name%", args[2]).replace("%player%", args[1])));
            return true;
        } else if(args[0].equalsIgnoreCase("list")) {
            String dot = plugin.getConfig().getString("Messages.list.dot");
            String dt = "";
            String classes = "";
            for(Class<? extends CraftTrail> t : CraftTrail.registration) {
                classes += dt + t.getSimpleName();
                dt = dot;
            }
            dt = "";
            String particles = "";
            for(Particle particle : Particle.values()) {
                particles += dt + particle.name();
                dt = dot;
            }
            for(String s : plugin.getConfig().getStringList("Messages.list.format")) {
                sender.sendMessage(BosterParticles.toColor(s.replace("%classes%", classes).replace("%particles%", particles)));
            }
            return true;
        } else {
            sendHelp(sender);
            return false;
        }
    }

    public void sendHelp(CommandSender sender) {
        for(String s : plugin.getConfig().getStringList("Messages.help")) {
            sender.sendMessage(BosterParticles.toColor(s));
        }
    }
}
