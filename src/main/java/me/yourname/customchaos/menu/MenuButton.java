package me.yourname.customchaos.menu;

import org.bukkit.Material;

import java.util.List;

/**
 * Describes one clickable command button inside a Chaos Control Menu category.
 *
 * supportsSelf  - left-click runs the command on the clicking player.
 * supportsTarget - shift-click opens a player-head picker and runs the
 *                   command on whichever player is selected.
 * A button should set at least one of the two to true.
 */
public record MenuButton(
    String subCommandName,
    Material icon,
    String displayName,
    List<String> lore,
    boolean supportsSelf,
    boolean supportsTarget
) {
}
