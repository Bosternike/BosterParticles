package net.boster.particles.main.gui.manage;

import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.files.MenuFile;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.gui.button.GUIButton;
import net.boster.particles.main.gui.manage.chat.TypingUser;
import net.boster.particles.main.gui.manage.confirmation.ConfirmationGUI;
import net.boster.particles.main.gui.multipage.MultiPageFunctionalEntry;
import net.boster.particles.main.gui.multipage.MultiPageGUI;
import net.boster.particles.main.utils.ConfigUtils;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageMenuGUI {

    private static final ItemStack MENU_ITEM;
    private static final ItemStack CREATE;
    private static final ItemStack TITLE;
    private static final ItemStack PERMISSION;
    private static final ItemStack SIZE;
    private static final ItemStack PLACEHOLDERS;
    private static final List<String> MENU_ITEM_LORE = List.of("",
            "&cLeft click &7- &fto delete this item",
            "&aRight click &7- &fto manage this item",
            "",
            "&9&lSECTION:");

    private static final int[] slots = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};

    static {
        MENU_ITEM = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk0Yjc1OGNhNzU2NWFhYWVhMjcyZjkyNjExZjY0ODlmYzk3OGVhMTBlYzNhZTRkNmJlMjk4NWMxZjdjYTE3OCJ9fX0=");

        CREATE = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmI3OGVlYmYzYzE3ZGRkMzg5MmEzZDk1MjBlMzhkZWNmNzg1ZjA5ZjI2MDFkM2U0ODAwMWExYjhkNDhlYmYxZiJ9fX0=");
        ItemMeta cm = CREATE.getItemMeta();
        cm.setDisplayName("\u00a76Create new item");
        CREATE.setItemMeta(cm);

        TITLE = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI5MjFjMjY0NTY5NmYyMTZiMmVlYjRiYTZkOWUwYmQ2N2FiOGZlMjQ4NTNjYjBlMzBlZDRlOTQ5NTFkNmU3In19fQ==");
        ItemMeta tm = TITLE.getItemMeta();
        tm.setDisplayName("\u00a7dChange title");
        TITLE.setItemMeta(tm);

        PERMISSION = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNlNGI1MzNlNGJhMmRmZjdjMGZhOTBmNjdlOGJlZjM2NDI4YjZjYjA2YzQ1MjYyNjMxYjBiMjVkYjg1YiJ9fX0=");
        ItemMeta pm = PERMISSION.getItemMeta();
        pm.setDisplayName("\u00a75Set permission");
        PERMISSION.setItemMeta(pm);

        SIZE = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQ5NzY1MDQ1N2M2OWNmZmNiY2Y2ODBlZWI2YTIzMWE2YWJlYjAwY2NhNWVhZGQ3YzFjNjg5MDBkNzgyZTNiOSJ9fX0=");
        ItemMeta sm = SIZE.getItemMeta();
        sm.setDisplayName("\u00a7eChange size");
        SIZE.setItemMeta(sm);

        PLACEHOLDERS = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y2NmY3ZjAzMTI1Y2Y1NDczMzY5NmYzNjMyZjBkOWU2NDcwYmFhYjg0OTg0N2VhNWVhMmQ3OTE1NmFkMGY0MCJ9fX0=");
        ItemMeta psm = PLACEHOLDERS.getItemMeta();
        psm.setDisplayName("\u00a7aManage placeholders");
        PLACEHOLDERS.setItemMeta(psm);
    }

    public static ItemStack getCreateItem() {
        return CREATE.clone();
    }

    public static ItemStack getMenuItem() {
        return MENU_ITEM.clone();
    }

    public static ItemStack getTitleItem() {
        return TITLE.clone();
    }

    public static ItemStack getPermissionItem() {
        return PERMISSION.clone();
    }

    public static ItemStack getSizeItem() {
        return SIZE.clone();
    }

    public static ItemStack getPlaceholdersItem() {
        return PLACEHOLDERS.clone();
    }

    public static void open(@NotNull Player p, @NotNull ParticlesGUI particlesGUI) {
        List<MultiPageFunctionalEntry> e = new ArrayList<>();
        ConfigurationSection items = particlesGUI.getFile().getConfig().getConfigurationSection("Items");
        if(items != null) {
            e.addAll(items.getKeys(false).stream().map(i -> createMenuEntry(particlesGUI, items.getConfigurationSection(i))).collect(Collectors.toList()));
        }
        MultiPageGUI gui = new MultiPageGUI(Utils.toColor("&8BosterParticles menu: &b&l" + particlesGUI.getName()), 54, p, e, slots);

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

        addButtons(gui, p, particlesGUI);

        gui.open();
    }

    private static void addButtons(MultiPageGUI gui, Player p, ParticlesGUI particlesGUI) {
        gui.getButtons().put(46, new GUIButton() {
            @Override
            public int getSlot() {
                return 46;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return MenusListGUI.getBackwards();
            }

            @Override
            public void onClick(Player p) {
                gui.clear();
                MenusListGUI.open(p);
            }
        });

        gui.getButtons().put(47, new GUIButton() {
            @Override
            public int getSlot() {
                return 47;
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
                p.sendMessage(Utils.toColor("%prefix% &fPlease, type in chat the name for new item section."));

                new TypingUser(p) {
                    @Override
                    public void onChat(@NotNull Player p, @NotNull String input) {
                        if(particlesGUI.getFile().getConfig().getConfigurationSection("Items." + input) != null) {
                            p.sendMessage(Utils.toColor("%prefix% &fItem &c" + input + "&f already exists."));
                            return;
                        }

                        clear();
                        particlesGUI.clear();
                        gui.clear();
                        MenuFile nf = new MenuFile(particlesGUI.getFile().getFile());
                        nf.create();
                        createSection(particlesGUI, nf, input);
                        ParticlesGUI pg = new ParticlesGUI(nf);
                        p.sendMessage(Utils.toColor("%prefix% &fItem &a" + input + "&f has been created!"));
                        open(p, pg);
                    }

                    @Override
                    public void cancel() {
                        gui.open();
                    }
                };
            }
        });

        gui.getButtons().put(48, new GUIButton() {
            @Override
            public int getSlot() {
                return 48;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return getTitleItem();
            }

            @Override
            public void onClick(Player p) {
                gui.setClosed(true);
                p.closeInventory();
                for(String s : TypingUser.MESSAGE) {
                    p.sendMessage(Utils.toColor(s));
                }
                p.sendMessage(Utils.toColor("%prefix% &fPlease, type in chat the new title."));

                new TypingUser(p) {
                    @Override
                    public void onChat(@NotNull Player p, @NotNull String input) {
                        clear();
                        gui.clear();

                        particlesGUI.getFile().getConfig().set("Title", input);
                        particlesGUI.getFile().save();
                        particlesGUI.setTitle(Utils.toColor(input));

                        p.sendMessage(Utils.toColor("%prefix% &fTitle has been changed to &a" + input + "&f!"));
                        open(p, particlesGUI);
                    }

                    @Override
                    public void cancel() {
                        gui.open();
                    }
                };
            }
        });

        ItemStack item = MenusListGUI.getMenuItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.toColor("&fName&7: &d" + particlesGUI.getName()));
        List<String> lore = new ArrayList<>();
        MenusListGUI.MENU_ITEM_LORE.forEach(s -> lore.add(Utils.toColor(s
                .replace("%title%", particlesGUI.getTitle())
                .replace("%size%", Integer.toString(particlesGUI.getGui().getSize()))
                .replace("%permission%", "" + particlesGUI.getPermission())
                .replace("%commands%", particlesGUI.getCommands().toString())
                .replace("%items%", Integer.toString(particlesGUI.getGui().getButtons().size())))));
        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.getButtons().put(49, new GUIButton() {
            @Override
            public int getSlot() {
                return 49;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return item;
            }
        });

        gui.getButtons().put(50, new GUIButton() {
            @Override
            public int getSlot() {
                return 50;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return getPermissionItem();
            }

            @Override
            public void onClick(Player p) {
                gui.setClosed(true);
                p.closeInventory();
                for(String s : TypingUser.MESSAGE) {
                    p.sendMessage(Utils.toColor(s));
                }
                p.sendMessage(Utils.toColor("%prefix% &fPlease, type in chat the new permission."));
                p.sendMessage(Utils.toColor("%prefix% &fIf you want to &b&lremove &fpermission, type &enull&f."));

                new TypingUser(p) {
                    @Override
                    public void onChat(@NotNull Player p, @NotNull String input) {
                        clear();
                        gui.clear();

                        String perm = input.equalsIgnoreCase("null") ? null : input;
                        particlesGUI.getFile().getConfig().set("Permission", perm);
                        particlesGUI.getFile().save();
                        particlesGUI.setPermission(perm);

                        p.sendMessage(Utils.toColor("%prefix% &fPermission has been changed to &a" + input + "&f!"));
                        open(p, particlesGUI);
                    }

                    @Override
                    public void cancel() {
                        gui.open();
                    }
                };
            }
        });

        gui.getButtons().put(51, new GUIButton() {
            @Override
            public int getSlot() {
                return 51;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return getSizeItem();
            }

            @Override
            public void onClick(Player p) {
                gui.setClosed(true);
                p.closeInventory();
                for(String s : TypingUser.MESSAGE) {
                    p.sendMessage(Utils.toColor(s));
                }
                p.sendMessage(Utils.toColor("%prefix% &fPlease, type in chat the new size."));

                new TypingUser(p) {
                    @Override
                    public void onChat(@NotNull Player p, @NotNull String input) {
                        int size;
                        try {
                            size = Integer.parseInt(input);
                        } catch (Exception e) {
                            p.sendMessage(Utils.toColor("%prefix% &fArgument &c" + input + "&f is not an integer."));
                            return;
                        }

                        if(!particlesGUI.setSize(size)) {
                            p.sendMessage(Utils.toColor("%prefix% &fArgument &c" + input + "&f can't be set as size."));
                            return;
                        }

                        clear();
                        gui.clear();

                        particlesGUI.getFile().getConfig().set("Size", size);
                        particlesGUI.getFile().save();
                        particlesGUI.clear();

                        MenuFile nf = new MenuFile(particlesGUI.getFile().getFile());
                        nf.create();
                        ParticlesGUI pg = new ParticlesGUI(nf);

                        p.sendMessage(Utils.toColor("%prefix% &fSize has been changed to &a" + input + "&f!"));
                        open(p, pg);
                    }

                    @Override
                    public void cancel() {
                        gui.open();
                    }
                };
            }
        });

        gui.getButtons().put(52, new GUIButton() {
            @Override
            public int getSlot() {
                return 52;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return getPlaceholdersItem();
            }

            @Override
            public void onClick(Player p) {
                gui.clear();
                ManagePlaceholdersGUI.open(p, particlesGUI);
            }
        });
    }

    private static MultiPageFunctionalEntry createMenuEntry(ParticlesGUI gui, ConfigurationSection section) {
        ItemStack item = getMenuItem();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.toColor("&fName&7: &d" + section.getName()));
        List<String> lore = new ArrayList<>();
        MENU_ITEM_LORE.forEach(s -> lore.add(Utils.toColor(s)));
        ConfigUtils.readSection(section, (s) -> "&f" + s + "&7: ", (s) -> "&6" + s).forEach(s -> lore.add(Utils.toColor(s)));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return new MultiPageFunctionalEntry() {
            @Override
            public ItemStack item(Player p, int page, int slot) {
                return item;
            }

            @Override
            public void onLeftClick(MultiPageGUI mpg, Player p, int page, int slot) {
                mpg.setClosed(true);
                new ConfirmationGUI(p) {
                    @Override
                    public void onAccept() {
                        MenuFile f = gui.getFile();
                        f.getConfig().set("Items." + section.getName(), null);
                        f.save();
                        gui.clear();
                        ParticlesGUI ng = new ParticlesGUI(f);
                        p.sendMessage(Utils.toColor("%prefix% &fItem &a" + section.getName() + "&f has been deleted!"));
                        setClosed(true);
                        ManageMenuGUI.open(p, ng);
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
                MenuSectionEditGUI.open(p, gui, section);
            }
        };
    }

    private static void createSection(ParticlesGUI gui, MenuFile file, String name) {
        for(int i = 0; i < gui.getSize(); i++) {
            if(gui.getGui().getButton(i) == null) {
                file.getConfig().set("Items." + name + ".slot", i);
                break;
            }
        }
        file.getConfig().set("Items." + name + ".material", "BARRIER");
        file.getConfig().set("Items." + name + ".name", "New item");
        file.save();
    }
}
