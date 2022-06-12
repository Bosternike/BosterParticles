package net.boster.particles.main.lib;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;

public class VaultSupport {
	
	private static boolean isLoaded = false;

	private static Economy eco = null;
	
	public static void load() {
		try {
			Class.forName("net.milkbowl.vault.economy.Economy");
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			eco = rsp.getProvider();
			isLoaded = true;
		} catch (ClassNotFoundException | NoClassDefFoundError | NullPointerException ignored) {}
    }

	public static double getBalance(@NotNull Player p) {
		if(isLoaded) {
			return eco.getBalance(p);
		}

		return 0;
	}

	public static void withdrawMoney(@NotNull Player p, double d) {
		if(isLoaded) {
			eco.withdrawPlayer(p, d);
		}
	}

	public static void depositMoney(@NotNull Player p, double d) {
		if(isLoaded) {
			eco.depositPlayer(p, d);
		}
	}
}
