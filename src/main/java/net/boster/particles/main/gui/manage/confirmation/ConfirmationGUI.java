package net.boster.particles.main.gui.manage.confirmation;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class ConfirmationGUI implements ConfirmActionGUI {

    public static final Map<Player, ConfirmationGUI> map = new HashMap<>();

    @Getter @NotNull private final Player player;

    @Getter @Setter private boolean closed = false;

    private static final ItemStack BORDER;

    private static final ItemStack ACCEPT;
    private static final ItemStack DENY;

    static {
        BORDER = new ItemStack(Material.VINE);
        ItemMeta meta = BORDER.getItemMeta();
        meta.setDisplayName("\u00a7f ");
        BORDER.setItemMeta(meta);

        ACCEPT = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDJjMjI2MGI3MDI3YzM1NDg2NWU4M2MxMjlmMmQzMzJmZmViZGJiODVmNjY0NjliYmY5ZmQyMGYzYzNjNjA3NyJ9fX0=");
        ItemMeta am = ACCEPT.getItemMeta();
        am.setDisplayName("\u00a7aConfirm action");
        ACCEPT.setItemMeta(am);

        DENY = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQwYTE0MjA4NDRjZTIzN2E0NWQyZTdlNTQ0ZDEzNTg0MWU5ZjgyZDA5ZTIwMzI2N2NmODg5NmM4NTE1ZTM2MCJ9fX0=");
        ItemMeta dm = DENY.getItemMeta();
        dm.setDisplayName("\u00a7cCancel action");
        DENY.setItemMeta(dm);
    }

    public ConfirmationGUI(@NotNull Player p) {
        this.player = p;
    }

    public void open() {
        open(Utils.toColor("&aConfirm action"));
    }

    public void open(@NotNull String title) {
        Inventory gui = Bukkit.createInventory(null, 27, title);

        for(int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, BORDER);
        }

        gui.setItem(getAcceptSlot(), acceptItem());
        gui.setItem(getDenySlot(), denyItemStack());

        player.openInventory(gui);
        map.put(player, this);
    }

    public void clear() {
        map.remove(player);
    }

    public static ItemStack getBorder() {
        return BORDER.clone();
    }

    public static ItemStack getAccept() {
        return ACCEPT.clone();
    }

    public static ItemStack getDeny() {
        return DENY.clone();
    }
}
