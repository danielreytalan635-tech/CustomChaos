package me.yourname.customchaos.menu;

import me.yourname.customchaos.CommandRouter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds and opens the Chaos Control Menu inventories: the main category
 * page, per-category button pages, and the target-select page used when a
 * button is shift-clicked.
 *
 * This is a thin presentation layer over the existing CommandRouter /
 * SubCommand architecture - every click ultimately calls the same
 * SubCommand.perform(player, args) that "/chaos <name> ..." already runs,
 * so permission checks and safety logic all stay in one place.
 */
public class ChaosMenu {

    private final CommandRouter commandRouter;
    private final List<MenuCategory> categories;

    public ChaosMenu(CommandRouter commandRouter) {
        this.commandRouter = commandRouter;
        this.categories = buildCategories();
    }

    public CommandRouter getCommandRouter() {
        return commandRouter;
    }

    public List<MenuCategory> getCategories() {
        return categories;
    }

    public void openMainMenu(Player player) {
        ChaosMenuHolder holder = new ChaosMenuHolder(ChaosMenuHolder.MenuType.MAIN, null, null);
        Inventory inventory = Bukkit.createInventory(holder, 27, Component.text("Chaos Control Menu", NamedTextColor.DARK_PURPLE));
        holder.setInventory(inventory);

        int slot = 10;
        for (MenuCategory category : categories) {
            if (slot > 16) {
                break; // One row of categories fits for now; add rows as more categories are added.
            }
            inventory.setItem(slot, buildCategoryIcon(category));
            slot++;
        }

        player.openInventory(inventory);
    }

    public void openCategory(Player player, MenuCategory category) {
        ChaosMenuHolder holder = new ChaosMenuHolder(ChaosMenuHolder.MenuType.CATEGORY, category, null);
        Inventory inventory = Bukkit.createInventory(holder, 27,
            Component.text("Chaos: " + category.displayName(), NamedTextColor.DARK_PURPLE));
        holder.setInventory(inventory);

        int slot = 0;
        for (MenuButton button : category.buttons()) {
            if (slot >= 26) {
                break;
            }
            inventory.setItem(slot, buildButtonIcon(button));
            slot++;
        }

        inventory.setItem(26, buildBackIcon());

        player.openInventory(inventory);
    }

    public void openTargetSelect(Player player, MenuCategory category, MenuButton button) {
        ChaosMenuHolder holder = new ChaosMenuHolder(ChaosMenuHolder.MenuType.TARGET_SELECT, category, button);
        Inventory inventory = Bukkit.createInventory(holder, 54,
            Component.text("Chaos: Select Target", NamedTextColor.DARK_PURPLE));
        holder.setInventory(inventory);

        int slot = 0;
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (slot >= 53) {
                break;
            }
            inventory.setItem(slot, buildPlayerHead(online));
            slot++;
        }

        inventory.setItem(53, buildBackIcon());

        player.openInventory(inventory);
    }

    private List<MenuCategory> buildCategories() {
        MenuCategory trolls = new MenuCategory("trolls", "Trolls", Material.ANVIL, List.of(
            new MenuButton("glowing", Material.GLOW_INK_SAC, "Glowing", List.of("Toggles a glow outline."), false, true),
            new MenuButton("keyboarddisconnect", Material.REDSTONE_TORCH, "Keyboard Disconnect", List.of("Freezes movement for 10s."), false, true)
        ));

        MenuCategory physics = new MenuCategory("physics", "Physics", Material.FEATHER, List.of(
            new MenuButton("yeet", Material.SLIME_BALL, "Yeet", List.of("Launches into the sky."), true, true),
            new MenuButton("slap", Material.LEATHER, "Slap", List.of("Knockback burst."), false, true),
            new MenuButton("earthquake", Material.COBBLESTONE, "Earthquake", List.of("Shakes nearby players."), true, false)
        ));

        MenuCategory magic = new MenuCategory("magic", "Magic", Material.BLAZE_POWDER, List.of(
            new MenuButton("firebender", Material.BLAZE_POWDER, "Firebender", List.of("Fire stream from your view."), true, false),
            new MenuButton("flight", Material.FEATHER, "Flight", List.of("Toggles flight for yourself."), true, false),
            new MenuButton("nightvision", Material.GOLDEN_CARROT, "Night Vision", List.of("5 minutes of night vision."), true, false),
            new MenuButton("swarm", Material.HONEYCOMB, "Bee Swarm", List.of("Summons bees around a target."), true, true),
            new MenuButton("timestop", Material.CLOCK, "Timestop", List.of("Freezes a target briefly."), false, true),
            new MenuButton("healanimation", Material.GOLDEN_APPLE, "Heal", List.of("Fully heals with an effect."), true, true)
        ));

        MenuCategory world = new MenuCategory("world", "World", Material.GRASS_BLOCK, List.of(
            new MenuButton("bloodmoon", Material.RED_DYE, "Blood Moon", List.of("Stormy night in your world."), true, false)
        ));

        MenuCategory cosmetics = new MenuCategory("cosmetics", "Cosmetics", Material.NETHER_STAR, List.of(
            new MenuButton("confetti", Material.FIREWORK_STAR, "Confetti", List.of("Colorful particle burst."), true, false)
        ));

        MenuCategory system = new MenuCategory("system", "System", Material.REDSTONE, List.of(
            new MenuButton("coinflip", Material.GOLD_NUGGET, "Coinflip", List.of("Heads or tails."), true, false),
            new MenuButton("roll", Material.BONE, "Roll", List.of("Rolls a 6-sided die."), true, false),
            new MenuButton("tps", Material.COMPARATOR, "TPS", List.of("Shows server TPS."), true, false),
            new MenuButton("ping", Material.ENDER_PEARL, "Ping", List.of("Shows ping in ms."), true, true),
            new MenuButton("randomteleport", Material.ENDER_EYE, "Random Teleport", List.of("Teleports nearby randomly."), true, false)
        ));

        return List.of(trolls, physics, magic, world, cosmetics, system);
    }

    private ItemStack buildCategoryIcon(MenuCategory category) {
        ItemStack item = new ItemStack(category.icon());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(category.displayName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));
        meta.lore(List.of(Component.text("Click to browse", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack buildButtonIcon(MenuButton button) {
        ItemStack item = new ItemStack(button.icon());
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(button.displayName(), NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));

        List<Component> lore = new ArrayList<>();
        for (String line : button.lore()) {
            lore.add(Component.text(line, NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        }
        if (button.supportsSelf()) {
            lore.add(Component.text("Left-click: use on yourself", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
        }
        if (button.supportsTarget()) {
            lore.add(Component.text("Shift-click: pick a target player", NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack buildPlayerHead(Player target) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(target);
            skullMeta.displayName(Component.text(target.getName(), NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            item.setItemMeta(skullMeta);
        }
        return item;
    }

    private ItemStack buildBackIcon() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Back", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);
        return item;
    }
}
