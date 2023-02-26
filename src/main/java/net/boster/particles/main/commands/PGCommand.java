package net.boster.particles.main.commands;

import com.google.common.collect.Lists;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.utils.ConfigValues;
import net.boster.particles.main.utils.Utils;
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
            List<String> aliases = Lists.newCopyOnWriteArrayList(gui.getCommands());
            setAliases(aliases.subList(1, aliases.size()));
        }
        register();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            if(!checkPlayer(sender)) return false;

            if(gui.getPermission() != null && !PlayerData.get((Player) sender).hasPermission(gui.getPermission())) {
                sendNoPerms(sender);
                return false;
            }

            gui.open(PlayerData.get((Player) sender));
        } else {
            if(gui.getPermission() != null && !sender.hasPermission(gui.getPermission() + ".others")) {
                sendNoPerms(sender);
            }

            Player p = Bukkit.getPlayer(args[0]);

            if(p == null) {
                sender.sendMessage(Utils.toColor(getLocaleMessage(sender, "Messages.open.nullPlayer").replace("%name%", args[0])));
                return false;
            }

            gui.open(PlayerData.get(p));
            sender.sendMessage(Utils.toColor(getLocaleMessage(sender, "Messages.open.success").replace("%gui%", gui.getName()).replace("%player%", args[0])));
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if(gui.getPermission() != null && !sender.hasPermission(gui.getPermission() + ".others")) {
            return Collections.emptyList();
        }

        return createDefaultTabComplete(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args, 0);
    }

    public void sendNoPerms(CommandSender sender) {
        if(gui.getNoPermissionMessage() != null) {
            PlayerData p = sender instanceof Player ? PlayerData.get((Player) sender) : null;
            sender.sendMessage(Utils.toColor(gui.getNoPermissionMessage().get(p != null ? p.getLocale() : ConfigValues.DEFAULT_LOCALE)));
        }
    }
}
