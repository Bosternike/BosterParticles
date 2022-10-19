package net.boster.particles.main.gui.manage;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.boster.gui.button.GUIButton;
import net.boster.gui.multipage.MultiPageFunctionalEntry;
import net.boster.gui.multipage.MultiPageGUI;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.data.PlayerData;
import net.boster.particles.main.files.CustomFile;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.gui.manage.chat.TypingUser;
import net.boster.particles.main.gui.manage.confirmation.ConfirmationGUI;
import net.boster.particles.main.utils.ConfigUtils;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenusListGUI {

    private static final ItemStack MENU_ITEM;
    private static final ItemStack BACKWARDS;
    private static final ItemStack CREATE;

    public static final List<String> MENU_ITEM_LORE_ACTIONS = Lists.newArrayList("", "&cLeft click &7- &fto delete this menu",
            "&aRight click &7- &fto manage this menu");
    public static final List<String> MENU_ITEM_LORE = Lists.newArrayList("",
            "&9&lINFO:",
            "&6>> &fTitle: &7%title%",
            "&6>> &fSize: &a%size%",
            "&6>> &fPermission: &c%permission%",
            "&6>> &fCommands: &a%commands%",
            "&6>> &fItems count: &a%items%");

    public static final List<Integer> slots = ImmutableList.of(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

    static {
        MENU_ITEM = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmNkYzBmZWI3MDAxZTJjMTBmZDUwNjZlNTAxYjg3ZTNkNjQ3OTMwOTJiODVhNTBjODU2ZDk2MmY4YmU5MmM3OCJ9fX0=");

        BACKWARDS = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2JkZjJjMzliYjVjYmEyNDQzMjllMDI4MGMwYjRhNDNlOWMzY2VhMjllMDZhYzIyMjcyMjM4ZmZiM2Q1ZTUzYiJ9fX0=");
        ItemMeta bm = BACKWARDS.getItemMeta();
        bm.setDisplayName("\u00a76Back");
        BACKWARDS.setItemMeta(bm);

        CREATE = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzU0ZGVlZWFiM2I5NzUwYzc3NjQyZWM5NWUzN2ZlMmJkZjlhZGM1NTVlMDgyNmRlZGQ3NjliZWRkMTAifX19");
        ItemMeta cm = BACKWARDS.getItemMeta();
        cm.setDisplayName("\u00a76Create new menu");
        CREATE.setItemMeta(cm);
    }

    public static void open(@NotNull Player p) {
        PlayerData d = PlayerData.get(p);

        MultiPageGUI gui = new MultiPageGUI(p);
        gui.setTitle(Utils.toColor("&d&lBosterParticles menus"));
        gui.setSize(54);
        gui.setSlots(slots);
        gui.setItems(ParticlesGUI.guis().stream().map(g -> createMenuEntry(d, g)).collect(Collectors.toList()));

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

        gui.getButtons().put(49, new GUIButton() {
            @Override
            public int getSlot() {
                return 49;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return getCreateItem();
            }

            @Override
            public void onClick(Player p) {
                gui.setClosed(true);
                p.closeInventory();
                for(String s : TypingUser.MESSAGE) {
                    p.sendMessage(Utils.toColor(s));
                }
                p.sendMessage(Utils.toColor("%prefix% &fPlease, type in chat the name for new menu."));

                new TypingUser(p) {
                    @Override
                    public void onChat(@NotNull Player p, @NotNull String input) {
                        if(MenuFile.get(input) != null) {
                            p.sendMessage(Utils.toColor("%prefix% &fMenu &c" + input + "&f already exists."));
                            return;
                        }

                        clear();
                        gui.clear();
                        CustomFile f = ConfigUtils.createMenu(input);
                        MenuFile mf = new MenuFile(f.getFile());
                        mf.create();
                        new ParticlesGUI(mf);
                        p.sendMessage(Utils.toColor("%prefix% &fMenu &a" + input + "&f has been created!"));
                        open(p);
                    }

                    @Override
                    public void cancel() {
                        gui.open();
                    }
                };
            }
        });

        gui.open();
    }

    public static ItemStack getBackwards() {
        return BACKWARDS.clone();
    }

    public static ItemStack getMenuItem() {
        return MENU_ITEM.clone();
    }

    public static ItemStack getCreateItem() {
        return CREATE.clone();
    }

    private static MultiPageFunctionalEntry createMenuEntry(PlayerData d, ParticlesGUI gui) {
        ItemStack item = getMenuItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.toColor("&fName&7: &d" + gui.getName()));
        List<String> lore = new ArrayList<>();

        String title = gui.getTitle().get(d.getLocale());
        String ts = title = title != null ? title : "null";

        MENU_ITEM_LORE_ACTIONS.forEach(s -> lore.add(Utils.toColor(s)));
        MENU_ITEM_LORE.forEach(s -> lore.add(Utils.toColor(s
                .replace("%title%", ts)
                .replace("%size%", Integer.toString(gui.getGui().getSize()))
                .replace("%permission%", "" + gui.getPermission())
                .replace("%commands%", gui.getCommands().toString())
                .replace("%items%", Integer.toString(gui.getGui().getButtons().size())))));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new MultiPageFunctionalEntry() {
            @Override
            public ItemStack item(Player p, int page, int slot) {
                return item;
            }

            @Override
            public void onClick(@NotNull MultiPageGUI mpg, @NotNull InventoryClickEvent e) {
                Player p = (Player) e.getWhoClicked();
                if(e.isLeftClick()) {
                    mpg.setClosed(true);
                    new ConfirmationGUI(p) {
                        @Override
                        public void onAccept() {
                            gui.delete();
                            p.sendMessage(Utils.toColor("%prefix% &fGUI &a" + gui.getName() + "&f has been deleted!"));
                            setClosed(true);
                            MenusListGUI.open(p);
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
                } else {
                    mpg.clear();
                    ManageMenuGUI.open(p, gui);
                }
            }
        };
    }
}
