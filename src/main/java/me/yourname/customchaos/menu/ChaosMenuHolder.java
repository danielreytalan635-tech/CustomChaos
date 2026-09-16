package me.yourname.customchaos.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

/**
 * Marks an Inventory as belonging to the Chaos Control Menu system, and
 * carries the context (menu type, category, pending button) the listener
 * needs to route a click correctly. Using a holder instead of matching on
 * inventory title means the listener never mistakes an unrelated inventory
 * (or one from another plugin) for one of ours.
 */
public class ChaosMenuHolder implements InventoryHolder {

    public enum MenuType {
        MAIN,
        CATEGORY,
        TARGET_SELECT
    }

    private final MenuType menuType;
    private final MenuCategory category;
    private final MenuButton pendingButton;
    private Inventory inventory;

    public ChaosMenuHolder(MenuType menuType, MenuCategory category, MenuButton pendingButton) {
        this.menuType = menuType;
        this.category = category;
        this.pendingButton = pendingButton;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public MenuButton getPendingButton() {
        return pendingButton;
    }
}
