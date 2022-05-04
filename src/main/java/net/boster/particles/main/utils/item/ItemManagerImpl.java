package net.boster.particles.main.utils.item;

import net.boster.particles.main.utils.Version;
import net.boster.particles.main.utils.item.creator.ItemCreator;
import net.boster.particles.main.utils.item.creator.NewItemCreator;
import net.boster.particles.main.utils.item.creator.OldItemCreator;
import net.boster.particles.main.utils.item.custommodeldata.CustomModelDataProvider;
import net.boster.particles.main.utils.item.custommodeldata.NewCustomModelDataProvider;
import net.boster.particles.main.utils.item.custommodeldata.OldCustomModelDataProvider;
import net.boster.particles.main.utils.item.owner.NewOwningPlayerProvider;
import net.boster.particles.main.utils.item.owner.OldOwningPlayerProvider;
import net.boster.particles.main.utils.item.owner.OwningPlayerProvider;
import net.boster.particles.main.utils.item.unbreakable.NewUnbreakableProvider;
import net.boster.particles.main.utils.item.unbreakable.OldUnbreakableProvider;
import net.boster.particles.main.utils.item.unbreakable.UnbreakableProvider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class ItemManagerImpl implements ItemManager {

    private final ItemCreator creator;
    private final CustomModelDataProvider customModelDataProvider;
    private final OwningPlayerProvider owningPlayerProvider;
    private final UnbreakableProvider unbreakableProvider;

    public ItemManagerImpl() {
        if(Version.getCurrentVersion().getVersionInteger() < 9) {
            this.creator = new OldItemCreator();
        } else {
            this.creator = new NewItemCreator();
        }

        if(Version.getCurrentVersion().getVersionInteger() < 10) {
            this.customModelDataProvider = new OldCustomModelDataProvider();
        } else {
            this.customModelDataProvider = new NewCustomModelDataProvider();
        }

        if(Version.getCurrentVersion().getVersionInteger() < 8) {
            this.owningPlayerProvider = new OldOwningPlayerProvider();
        } else {
            this.owningPlayerProvider = new NewOwningPlayerProvider();
        }

        if(Version.getCurrentVersion().getVersionInteger() < 7) {
            this.unbreakableProvider = new OldUnbreakableProvider();
        } else {
            this.unbreakableProvider = new NewUnbreakableProvider();
        }
    }

    @Override
    public ItemStack createItem(String s) throws Exception {
        return creator.createItem(s);
    }

    @Override
    public void setCustomModelData(@NotNull ItemMeta meta, int i) {
        customModelDataProvider.setCustomModelData(meta, i);
    }

    @Override
    public void setOwner(@NotNull SkullMeta meta, @NotNull String player) {
        owningPlayerProvider.setOwner(meta, player);
    }

    @Override
    public void setUnbreakable(@NotNull ItemMeta meta, boolean b) {
        unbreakableProvider.setUnbreakable(meta, b);
    }
}
