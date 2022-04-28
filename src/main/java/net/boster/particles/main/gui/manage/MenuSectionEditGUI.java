package net.boster.particles.main.gui.manage;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.CustomGUI;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.gui.button.GUIButton;
import net.boster.particles.main.gui.manage.chat.TypingUser;
import net.boster.particles.main.gui.manage.confirmation.ConfirmationGUI;
import net.boster.particles.main.gui.manage.translator.*;
import net.boster.particles.main.gui.multipage.MultiPageFunctionalEntry;
import net.boster.particles.main.gui.multipage.MultiPageGUI;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuSectionEditGUI {

    private static final ItemStack PATH_ITEM;

    static {
        PATH_ITEM = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjAyYTExNjkzMDlmMDVlZjJmMDYxYjFmYTBmZTIyNWYyOWQ3M2EyNGY4ZjA3Y2NjMmE3MDVkZWVhY2EwNjlkMSJ9fX0=");
    }

    public static ItemStack getPathItem() {
        return PATH_ITEM.clone();
    }

    public static void open(@NotNull Player p, @NotNull ParticlesGUI particlesGUI, @NotNull ConfigurationSection section) {
        MultiPageGUI gui = new MultiPageGUI(Utils.toColor("&8Section editor: &b&l" + section.getName()), 54, p,
                createButtons(p, particlesGUI, section), MenusListGUI.slots);
        for(int i : Utils.createBorder(54)) {
            gui.getButtons().put(i, new GUIButton() {
                @Override
                public int getSlot() {
                    return i;
                }

                @Override
                public ItemStack prepareItem(Player p) {
                    return ConfirmationGUI.getBorder();
                }
            });
        }

        gui.open();
    }

    private static List<MultiPageFunctionalEntry> createButtons(@NotNull Player p, @NotNull ParticlesGUI particlesGUI, @NotNull ConfigurationSection section) {
        List<MultiPageFunctionalEntry> list = new ArrayList<>();

        list.add(getEntry(p, particlesGUI, section, "permission", null));
        list.add(getEntry(p, particlesGUI, section, "cost", new DoubleTranslator()));
        list.add(getEntry(p, particlesGUI, section, "slot", new IntegerTranslator()));
        list.add(getEntry(p, particlesGUI, section, "blank", new BooleanTranslator()));
        list.add(getEntry(p, particlesGUI, section, "material", new MaterialTranslator()));
        list.add(getEntry(p, particlesGUI, section, "skull", null));
        list.add(getEntry(p, particlesGUI, section, "head", null));
        list.add(getEntry(p, particlesGUI, section, "name", null));
        list.add(getEntry(p, particlesGUI, section, "success_actions.sound", new BosterSoundTranslator()));
        list.add(getEntry(p, particlesGUI, section, "no_permission_actions.sound", new BosterSoundTranslator()));
        list.add(getEntry(p, particlesGUI, section, "not_enough_money_actions.sound", new BosterSoundTranslator()));

        return list;
    }

    private static <T> MultiPageFunctionalEntry getEntry(@NotNull Player p, @NotNull ParticlesGUI particlesGUI, @NotNull ConfigurationSection section, @NotNull String path, @Nullable ITranslator translator) {
        ItemStack item = getPathItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a7fPath: \u00a7d" + path);
        meta.setLore(List.of("",
                Utils.toColor("&fCurrent value&7: " + section.getString(path, "&cnull")),
                "",
                Utils.toColor("&cLeft click &7-&f to delete this path"),
                Utils.toColor("&aRight click &7-&f to edit this path")));
        item.setItemMeta(meta);

        return new MultiPageFunctionalEntry() {
            ParticlesGUI saveAndUpdate() {
                particlesGUI.getFile().save();
                particlesGUI.clear();

                MenuFile nf = new MenuFile(particlesGUI.getFile().getFile());
                nf.create();
                return new ParticlesGUI(nf);
            }

            @Override
            public @Nullable ItemStack item(Player p, int page, int slot) {
                return item;
            }

            @Override
            public void onLeftClick(MultiPageGUI mpg, Player p, int page, int slot) {
                mpg.setClosed(true);
                new ConfirmationGUI(p) {
                    @Override
                    public void onAccept() {
                        section.set(path, null);

                        ParticlesGUI pg = saveAndUpdate();

                        p.sendMessage(Utils.toColor("%prefix% &fPath &a" + path + "&f has been deleted!"));
                        setClosed(true);
                        MenuSectionEditGUI.open(p, pg, section);
                    }

                    @Override
                    public void onDeny() {
                        setClosed(true);
                        mpg.open();
                    }

                    @Override
                    public void close() {
                        Bukkit.getScheduler().runTaskLater(BosterParticles.getInstance(), mpg::open, 1);
                    }
                }.open();
            }

            @Override
            public void onRightClick(MultiPageGUI mpg, Player p, int page, int slot) {
                mpg.setClosed(true);
                p.closeInventory();
                for(String s : TypingUser.MESSAGE) {
                    p.sendMessage(Utils.toColor(s));
                }
                p.sendMessage(Utils.toColor("%prefix% &fPlease, type in chat the new value of path &6" + path));

                new TypingUser(p) {
                    @Override
                    public void onChat(@NotNull Player p, @NotNull String input) {
                        if(translator != null) {
                            Optional<T> o = translator.translate(p, input);

                            if(o.isEmpty()) {
                                p.sendMessage(Utils.toColor("%prefix% &fArgument &c" + input + "&f is not a valid value."));
                                return;
                            }

                            section.set(path, o.get());
                        } else {
                            section.set(path, input);
                        }

                        clear();
                        mpg.clear();

                        ParticlesGUI pg = saveAndUpdate();

                        p.sendMessage(Utils.toColor("%prefix% &fText of path &6" + path + "&f has been changed to &a" + input + "&f!"));
                        open(p, pg, section);
                    }

                    @Override
                    public void cancel() {
                        mpg.open();
                    }
                };
            }
        };
    }
}
