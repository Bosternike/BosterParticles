package net.boster.particles.main.gui.manage;

import net.boster.particles.main.gui.CustomGUI;
import net.boster.particles.main.gui.ParticlesGUI;
import net.boster.particles.main.gui.button.GUIButton;
import net.boster.particles.main.gui.manage.chat.TypingUser;
import net.boster.particles.main.gui.manage.confirmation.ConfirmationGUI;
import net.boster.particles.main.gui.placeholders.GUIPlaceholder;
import net.boster.particles.main.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManagePlaceholdersGUI {

    private static final ItemStack PLACEHOLDER;
    private static final List<String> PLACEHOLDER_LORE = List.of("",
            "&fCurrent text: &f%text%",
            "&fPlaceholder: &b%placeholder%",
            "",
            "&aClick here to change the text.");

    static {
        PLACEHOLDER = Utils.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJhMjQ1OWM4YzJiZWQ5ZTFkNTRiOWQyZDkxOGE0ZTBkNzM4NGIzY2Q5MGJkNjU3YWE3MjhjODBlYzRiZmVkMCJ9fX0=");
    }

    public static void open(@NotNull Player p, @NotNull ParticlesGUI particlesGUI) {
        CustomGUI gui = new CustomGUI(p, Utils.toColor("&8Placeholder editor: &b&l" + particlesGUI.getName()), 27);
        for(int i : Utils.createBorder(27)) {
            gui.getGui().addButton(new GUIButton() {
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

        gui.getGui().addButton(new GUIButton() {
            @Override
            public int getSlot() {
                return 22;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return MenusListGUI.getBackwards();
            }

            @Override
            public void onClick(Player p) {
                gui.clear();
                ManageMenuGUI.open(p, particlesGUI);
            }
        });

        int slot = 10;
        for(Map.Entry<String, GUIPlaceholder> placeholder : particlesGUI.getPlaceholders().placeholders()) {
            createPlaceholder(slot, gui, p, particlesGUI, placeholder.getKey());
            slot += 2;
        }

        gui.open();
    }

    private static void createPlaceholder(int slot, CustomGUI gui, Player p, ParticlesGUI particlesGUI, String placeholder) {
        ItemStack item = PLACEHOLDER.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.toColor("&fName&7: &d" + placeholder));
        List<String> lore = new ArrayList<>();
        PLACEHOLDER_LORE.forEach(s -> lore.add(Utils.toColor(s
                .replace("%text%", particlesGUI.getPlaceholders().getPlaceholder(placeholder))
                .replace("%placeholder%", placeholder))));
        meta.setLore(lore);
        item.setItemMeta(meta);

        gui.getGui().addButton(new GUIButton() {
            @Override
            public int getSlot() {
                return slot;
            }

            @Override
            public ItemStack prepareItem(Player p) {
                return item;
            }

            @Override
            public void onClick(Player p) {
                gui.setClosed(true);
                p.closeInventory();
                for(String s : TypingUser.MESSAGE) {
                    p.sendMessage(Utils.toColor(s));
                }
                p.sendMessage(Utils.toColor("%prefix% &fPlease, type in chat the new placeholder text."));

                new TypingUser(p) {
                    @Override
                    public void onChat(@NotNull Player p, @NotNull String input) {
                        clear();
                        gui.clear();

                        particlesGUI.getPlaceholders().setPlaceholder(placeholder, input, input);
                        particlesGUI.getFile().getConfig().set("Placeholders." + placeholder, input);
                        particlesGUI.getFile().save();

                        p.sendMessage(Utils.toColor("%prefix% &fText of placeholder &6" + placeholder + "&f was changed to &a" + input + "&f!"));
                        open(p, particlesGUI);
                    }

                    @Override
                    public void cancel() {
                        gui.open();
                    }
                };
            }
        });
    }
}
