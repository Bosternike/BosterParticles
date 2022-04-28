package net.boster.particles.main.gui.multipage;

import lombok.Getter;
import lombok.Setter;
import net.boster.particles.main.BosterParticles;
import net.boster.particles.main.gui.button.GUIButton;
import net.boster.particles.main.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MultiPageGUI {

    private static final int[] SLOTS_0 = new int[]{0, 1, 2, 3, 4, 5, 6, 9, 8};
    private static final int[] SLOTS_1 = new int[]{0, 1, 2, 3, 4, 5, 6, 9, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
    private static final int[] SLOTS_2 = new int[]{0, 1, 2, 3, 4, 5, 6, 9, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
    private static final int[] SLOTS_3 = new int[]{0, 1, 2, 3, 4, 5, 6, 9, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
    private static final int[] SLOTS_4 = new int[]{0, 1, 2, 3, 4, 5, 6, 9, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
    private static final int[] SLOTS_5 = new int[]{0, 1, 2, 3, 4, 5, 6, 9, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

    private static final HashMap<Player, MultiPageGUI> user = new HashMap<>();

    @Getter @NotNull private final List<MultiPageButton> nextButtons;
    @Getter @NotNull private final List<MultiPageButton> previousButtons;

    @Getter @Setter @Nullable private String title;
    @Getter final private int size;
    @Getter @Nullable Inventory gui;
    @Getter @NotNull private final Player p;
    @Getter private final int pages;
    @Getter private int pageNumber = 1;
    @Getter private int actualFrom = 0;
    @Getter private final int[] slots;

    @Getter @Setter @NotNull private Map<Integer, GUIButton> buttons = new HashMap<>();

    @Getter @NotNull private final List<MultiPageFunctionalEntry> items;
    @Getter @NotNull private final LinkedHashMap<Integer, MultiPageFunctionalEntry> currentItems = new LinkedHashMap<>();

    public boolean accessPlayerInventory = false;
    public List<Integer> accessibleSlots = new ArrayList<>();

    private BukkitTask closedTask;
    @Getter private boolean closed = false;

    public MultiPageGUI(@Nullable String title, int size, @NotNull Player p, @NotNull List<MultiPageFunctionalEntry> items, int[] slots, @NotNull List<MultiPageButton> nextButtons, @NotNull List<MultiPageButton> previousButtons) {
        user.put(p, this);
        this.title = title;
        this.size = size;
        this.p = p;
        this.items = items;
        this.slots = slots;
        this.pages = Utils.menuPages(items.size(), slots.length);
        this.nextButtons = nextButtons;
        this.previousButtons = previousButtons;
    }

    public MultiPageGUI(@Nullable String title, @NotNull Player p, @NotNull List<MultiPageFunctionalEntry> items, int[] slots, @NotNull List<MultiPageButton> nextButtons, @NotNull List<MultiPageButton> previousButtons) {
        this(title, 54, p, items, slots, nextButtons, previousButtons);
    }

    public MultiPageGUI(@Nullable String title, @NotNull Player p, @NotNull List<MultiPageFunctionalEntry> items, @NotNull List<MultiPageButton> nextButtons, @NotNull List<MultiPageButton> previousButtons) {
        this(title, 54, p, items, getSlots4(), nextButtons, previousButtons);
    }

    public MultiPageGUI(@Nullable String title, int size, @NotNull Player p, @NotNull List<MultiPageFunctionalEntry> items, int[] slots) {
        this(title, size, p, items, slots, createSwitchItemByDefault(size - 1, "&6Next page", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2MTg1YjFkNTE5YWRlNTg1ZjE4NGMzNGYzZjNlMjBiYjY0MWRlYjg3OWU4MTM3OGU0ZWFmMjA5Mjg3In19fQ==", true),
                createSwitchItemByDefault(Utils.getGUIFirstSlotOfLastRaw(size), "&6Next page", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ3M2NmNjZkMzFiODNjZDhiODY0NGMxNTk1OGMxYjczYzhkOTczMjNiODAxMTcwYzFkODg2NGJiNmE4NDZkIn19fQ==", false));
    }

    public MultiPageGUI(@Nullable String title, @NotNull Player p, @NotNull List<MultiPageFunctionalEntry> items, int[] slots) {
        this(title, 54, p, items, slots);
    }

    public MultiPageGUI(@Nullable String title, @NotNull Player p, @NotNull List<MultiPageFunctionalEntry> items) {
        this(title, 54, p, items, getSlots4());
    }

    public static @Nullable MultiPageGUI get(Player p) {
        return user.get(p);
    }

    public boolean newPage() {
        if(pageNumber < pages) {
            pageNumber = pageNumber + 1;
            actualFrom = actualFrom + slots.length;
            return true;
        } else {
            return false;
        }
    }

    public boolean pastPage() {
        if(pageNumber > 1) {
            pageNumber = pageNumber - 1;
            actualFrom = actualFrom - slots.length;
            return true;
        } else {
            return false;
        }
    }

    public void createGUI() {
        if(title != null) {
            gui = Bukkit.createInventory(null, size, title.replace("%page%", Integer.toString(pageNumber)));
        } else {
            gui = Bukkit.createInventory(null, size);
        }
    }

    public void prepare() {
        for(GUIButton b : buttons.values()) {
            ItemStack i = b.prepareItem(p);
            if(i != null) {
                gui.setItem(b.getSlot(), i);
            }
        }

        Arrays.stream(slots).forEach(i -> gui.setItem(i, null));
    }

    public void addSwitchers() {
        for(MultiPageButton i : nextButtons) {
            if(i.getAppearance().checkAppear(this, true)) {
                ItemStack item = i.item(p);
                if(item != null) {
                    gui.setItem(i.getSlot(), item);
                }
            }
        }
        for(MultiPageButton i : previousButtons) {
            if(i.getAppearance().checkAppear(this, false)) {
                ItemStack item = i.item(p);
                if(item != null) {
                    gui.setItem(i.getSlot(), item);
                }
            }
        }
    }

    public void loadItems() {
        for(int i = 0; i < slots.length; i++) {
            if(slots[i] >= gui.getSize()) continue;
            if(actualFrom + i >= items.size()) break;

            MultiPageFunctionalEntry e = items.get(actualFrom + i);
            gui.setItem(slots[i], e.item(p, pageNumber, slots[i]));
            currentItems.put(slots[i], e);
        }
    }

    public void open() {
        currentItems.clear();

        createGUI();
        prepare();
        addSwitchers();
        loadItems();

        setClosed(1);
        p.openInventory(gui);
    }

    public @Nullable MultiPageButton isSwitchSlot(@NotNull List<MultiPageButton> buttons, int slot) {
        for(MultiPageButton b : buttons) {
            if(b.getSlot() == slot) {
                return b;
            }
        }

        return null;
    }

    public void setClosed(boolean b) {
        closed = b;
    }

    public void setClosed(int ticks) {
        closed = true;

        if(closedTask != null) {
            closedTask.cancel();
        }

        Bukkit.getScheduler().runTaskLater(BosterParticles.getInstance(), () -> {
            closed = false;
            closedTask = null;
            if(!p.isOnline() || gui != p.getOpenInventory().getTopInventory()) {
                clear();
            }
        }, ticks);
    }

    public void clear() {
        user.remove(p);
    }

    public boolean checkAccess(int slot) {
        if(accessibleSlots == null) return false;

        return accessibleSlots.contains(slot);
    }

    private static List<MultiPageButton> createSwitchItemByDefault(int slot, String text, String skull, boolean next) {
        ItemStack item = Utils.getCustomSkull(skull);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.toColor(text));
        item.setItemMeta(meta);
        return List.of(new MultiPageButton() {
            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public @NotNull MultiPageButtonAppearance getAppearance() {
                return MultiPageButtonAppearance.NEEDED;
            }

            @Override
            public ItemStack item(Player p) {
                return item;
            }

            @Override
            public void performPage(MultiPageGUI gui) {
                boolean b;
                if(next) {
                    b = gui.newPage();
                } else {
                    b = gui.pastPage();
                }
                if(b) {
                    gui.open();
                }
            }
        });
    }

    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getHolder() instanceof Player) {
            e.setCancelled(!accessPlayerInventory);
            return;
        }

        e.setCancelled(!checkAccess(e.getSlot()));

        MultiPageButton mb = isSwitchSlot(nextButtons, e.getSlot());
        if(mb == null) {
            mb = isSwitchSlot(previousButtons, e.getSlot());
        }
        if(mb != null) {
            mb.performPage(this);
            if(e.isRightClick()) {
                mb.onRightClick(this, p, this.getPageNumber(), e.getSlot());
            } else {
                mb.onLeftClick(this, p, this.getPageNumber(), e.getSlot());
            }
            return;
        }

        MultiPageFunctionalEntry me = currentItems.get(e.getSlot());
        if(me != null) {
            if(e.isRightClick()) {
                me.onRightClick(this, p, pageNumber, e.getSlot());
            } else {
                me.onLeftClick(this, p, pageNumber, e.getSlot());
            }
            return;
        }

        GUIButton gb = buttons.get(e.getSlot());
        if(gb != null) {
            if(e.isRightClick()) {
                gb.onRightClick(p);
            } else {
                gb.onLeftClick(p);
            }
        }
    }

    public void onInventoryClosed(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTaskLater(BosterParticles.getInstance(), () -> {
            if(gui != p.getOpenInventory().getTopInventory()) {
                clear();
            }
        }, 1);
    }

    public static int[] getSlots0() {
        return Arrays.copyOf(SLOTS_0, SLOTS_0.length);
    }

    public static int[] getSlots1() {
        return Arrays.copyOf(SLOTS_1, SLOTS_1.length);
    }

    public static int[] getSlots2() {
        return Arrays.copyOf(SLOTS_2, SLOTS_2.length);
    }

    public static int[] getSlots3() {
        return Arrays.copyOf(SLOTS_3, SLOTS_3.length);
    }

    public static int[] getSlots4() {
        return Arrays.copyOf(SLOTS_4, SLOTS_4.length);
    }

    public static int[] getSlots5() {
        return Arrays.copyOf(SLOTS_5, SLOTS_5.length);
    }

    public static int[] getSlots(int[] slots, int from, int to) {
        if(from < 0) {
            throw new ArrayIndexOutOfBoundsException("index " + from + " is lower than 0");
        }
        if(to > slots.length) {
            throw new ArrayIndexOutOfBoundsException("index " + to + " is out of bounds " + slots.length);
        }

        int[] r = new int[to - from];
        for(int[] i = new int[]{0, from}; i[0] < r.length && i[1] < to; i[0]++, i[1]++) {
            r[i[0]] = slots[i[1]];
        }
        return r;
    }
}
