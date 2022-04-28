package net.boster.particles.main.lib;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultSupport {
	
	private static boolean isLoaded = false;

	private static Economy eco = null;
	
	public static void load() {
		try {
			try {
				Class.forName("net.milkbowl.vault.permission.Permission");
				RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
				eco = rsp.getProvider();
				isLoaded = true;
			} catch (ClassNotFoundException | NoClassDefFoundError | NullPointerException ignored) {}
		} catch (Exception ignored) {}
    }

	public static double getBalance(Player p) {
		if(isLoaded) {
			return eco.getBalance(Bukkit.getOfflinePlayer(p.getUniqueId()));
		}

		return 0;
	}

	public static void withdrawMoney(Player p, double d) {
		if(isLoaded) {
			eco.withdrawPlayer(Bukkit.getOfflinePlayer(p.getUniqueId()), d);
		}
	}
}
