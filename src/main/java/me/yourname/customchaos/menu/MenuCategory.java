package me.yourname.customchaos.menu;

import org.bukkit.Material;

import java.util.List;

public record MenuCategory(
    String key,
    String displayName,
    Material icon,
    List<MenuButton> buttons
) {
}
