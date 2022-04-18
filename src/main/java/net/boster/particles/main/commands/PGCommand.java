package net.boster.particles.main.commands;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.gui.ParticlesGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PGCommand extends BosterCommand {

    private final ParticlesGUI gui;

    public PGCommand(@NotNull ParticlesGUI gui) {
        super(BosterParticles.getInstance(), gui.getCommands().get(0));
        this.gui = gui;
        if(gui.getCommands().size() > 1) {
            List<String> aliases = List.copyOf(gui.getCommands());
            setAliases(aliases.subList(1, aliases.size()));
        }
        register();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        boolean b = gui.getPermission() != null;
        if(args.length == 0) {
            if(!checkPlayer(sender)) return false;

            if(b && !sender.hasPermission(gui.getPermission())) {
                if(gui.getNoPermissionMessage() != null) {
                    sender.sendMessage(BosterParticles.toColor(gui.getNoPermissionMessage()));
                }
            }

            gui.open((Player) sender);
            return true;
        } else {
            if(b && !sender.hasPermission(gui.getPermission() + ".others")) {
                if(gui.getNoPermissionMessage() != null) {
                    sender.sendMessage(BosterParticles.toColor(gui.getNoPermissionMessage()));
                }
            }

            Player p = Bukkit.getPlayer(args[0]);

            if(p == null) {
                sender.sendMessage(BosterParticles.toColor(plugin.getConfig().getString("Messages.open.nullPlayer").replace("%name%", args[0])));
                return false;
            }

            gui.open(p);
            sender.sendMessage(BosterParticles.toColor(plugin.getConfig().getString("Messages.open.success").replace("%gui%", gui.getName()).replace("%player%", args[0])));
            return true;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(gui.getPermission() != null && !sender.hasPermission(gui.getPermission() + ".others")) {
            return Collections.emptyList();
        }

        return createDefaultTabComplete(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args, 0);
    }
}
