package me.yourname.customchaos.menu;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/**
 * Handles every click inside Chaos Control Menu inventories. Identifies
 * "our" inventories via ChaosMenuHolder rather than by title, so it never
 * interferes with unrelated GUIs or the player's own inventory below it.
 *
 * Every branch ends by calling SubCommand.perform(...) on the same
 * registered command instance "/chaos <name>" would use, so permission
 * checks (chaos.freeze, chaos.sudo, etc.) run exactly the same way
 * regardless of whether the player typed the command or clicked it.
 */
public class ChaosMenuListener implements Listener {

    private final ChaosMenu chaosMenu;

    public ChaosMenuListener(ChaosMenu chaosMenu) {
        this.chaosMenu = chaosMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder rawHolder = event.getInventory().getHolder();
        if (!(rawHolder instanceof ChaosMenuHolder holder)) {
            return;
        }

        // Always cancel clicks inside our GUIs so nothing can be taken or moved.
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !clickedInventory.equals(event.getView().getTopInventory())) {
            return; // Ignore clicks in the player's own inventory below the GUI.
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir()) {
            return;
        }

        switch (holder.getMenuType()) {
            case MAIN -> handleMainClick(player, event.getSlot());
            case CATEGORY -> handleCategoryClick(player, holder, event.getSlot(), event.isShiftClick());
            case TARGET_SELECT -> handleTargetSelectClick(player, holder, clicked);
        }
    }

    private void handleMainClick(Player player, int slot) {
        List<MenuCategory> categories = chaosMenu.getCategories();
        int index = slot - 10;
        if (index < 0 || index >= categories.size()) {
            return;
        }
        chaosMenu.openCategory(player, categories.get(index));
    }

    private void handleCategoryClick(Player player, ChaosMenuHolder holder, int slot, boolean shiftClick) {
        if (slot == 26) {
            chaosMenu.openMainMenu(player);
            return;
        }

        MenuCategory category = holder.getCategory();
        if (category == null || slot < 0 || slot >= category.buttons().size()) {
            return;
        }

        MenuButton button = category.buttons().get(slot);

        if (shiftClick && button.supportsTarget()) {
            chaosMenu.openTargetSelect(player, category, button);
            return;
        }

        if (!button.supportsSelf()) {
            player.sendMessage(Component.text(
                "That action needs a target - shift-click it instead.", NamedTextColor.RED
            ));
            return;
        }

        executeSelf(player, button);
        player.closeInventory();
    }

    private void handleTargetSelectClick(Player player, ChaosMenuHolder holder, ItemStack clicked) {
        if (clicked.getType() == Material.BARRIER) {
            if (holder.getCategory() != null) {
                chaosMenu.openCategory(player, holder.getCategory());
            } else {
                chaosMenu.openMainMenu(player);
            }
            return;
        }

        if (!(clicked.getItemMeta() instanceof SkullMeta skullMeta) || skullMeta.getOwningPlayer() == null) {
            return;
        }

        MenuButton button = holder.getPendingButton();
        if (button == null) {
            return;
        }

        String targetName = skullMeta.getOwningPlayer().getName();
        if (targetName == null) {
            return;
        }

        executeOnTarget(player, button, targetName);
        player.closeInventory();
    }

    private void executeSelf(Player player, MenuButton button) {
        SubCommand subCommand = chaosMenu.getCommandRouter().getSubCommands().get(button.subCommandName().toLowerCase());
        if (subCommand == null) {
            player.sendMessage(Component.text("That command is not currently registered.", NamedTextColor.RED));
            return;
        }
        subCommand.perform(player, new String[]{button.subCommandName()});
    }

    private void executeOnTarget(Player player, MenuButton button, String targetName) {
        SubCommand subCommand = chaosMenu.getCommandRouter().getSubCommands().get(button.subCommandName().toLowerCase());
        if (subCommand == null) {
            player.sendMessage(Component.text("That command is not currently registered.", NamedTextColor.RED));
            return;
        }
        subCommand.perform(player, new String[]{button.subCommandName(), targetName});
    }
}
