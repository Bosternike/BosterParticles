package net.boster.particles.main.commands;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.utils.ReflectionUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BosterCommand extends Command implements PluginIdentifiableCommand {

    @Getter @NotNull private static final HashMap<String, BosterCommand> registeredCommands = new HashMap<>();

    @Getter @NotNull protected final BosterParticles plugin;
    @Getter @Setter @Nullable protected List<String> subCommands;

    public BosterCommand(@NotNull BosterParticles plugin, @NotNull String name, @NotNull List<String> aliases) {
        super(name);
        this.setAliases(aliases);
        this.plugin = plugin;
    }

    public BosterCommand(@NotNull BosterParticles plugin, @NotNull String name, @NotNull String... aliases) {
        this(plugin, name, Arrays.asList(aliases));
    }

    @Override
    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);

    public final void register() {
        ReflectionUtils.registerCommand(getName(), this);
        registeredCommands.put(getName(), this);
    }

    public final void unregister() {
        registeredCommands.remove(getName());
        ReflectionUtils.unregisterCommand(this);
    }

    public boolean checkPlayer(CommandSender sender) {
        if(sender instanceof Player) {
            return true;
        } else {
            sender.sendMessage("§b[§dBosterParticles§b] §cYou must be a player to perform this command.");
            return false;
        }
    }

    public static void load() {
        ReflectionUtils.syncCommands();
    }

    public static void unload() {
        for(BosterCommand cmd : registeredCommands.values()) {
            ReflectionUtils.unregisterCommand(cmd);
        }
        registeredCommands.clear();
        ReflectionUtils.syncCommands();
    }

    public List<String> createDefaultTabComplete(@NotNull List<String> vars, @NotNull String[] args, int arg) {
        if(args.length == arg + 1) {
            try {
                List<String> list = new ArrayList<>();
                for(String i : vars) {
                    if(i.toLowerCase().startsWith(args[arg].toLowerCase())) {
                        list.add(i);
                    }
                }
                return list;
            } catch (Exception e) {
                return vars;
            }
        }

        return Collections.emptyList();
    }
}
