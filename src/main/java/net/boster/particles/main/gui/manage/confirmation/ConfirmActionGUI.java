package net.boster.particles.main.gui.manage.confirmation;

import org.bukkit.inventory.ItemStack;

public interface ConfirmActionGUI {

    default ItemStack acceptItem() {
        return ConfirmationGUI.getAccept();
    }

    default ItemStack denyItemStack() {
        return ConfirmationGUI.getDeny();
    }

    default int getAcceptSlot() {
        return 12;
    }

    default int getDenySlot() {
        return 14;
    }

    void onAccept();
    void onDeny();

    void close();
}
