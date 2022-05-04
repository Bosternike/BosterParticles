package net.boster.particles.main.utils.item;

import net.boster.particles.main.utils.item.creator.ItemCreator;
import net.boster.particles.main.utils.item.custommodeldata.CustomModelDataProvider;
import net.boster.particles.main.utils.item.owner.OwningPlayerProvider;
import net.boster.particles.main.utils.item.unbreakable.UnbreakableProvider;

public interface ItemManager extends ItemCreator, CustomModelDataProvider, OwningPlayerProvider, UnbreakableProvider {
}
