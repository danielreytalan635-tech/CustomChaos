package me.yourname.customchaos.commands;

import me.yourname.customchaos.FreezeManager;
import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * "/chaos keyboarddisconnect <player> [seconds|stop]"
 *
 * Simulates a broken keyboard by freezing the target's movement for a set
 * duration (default 10s, capped at MAX_SECONDS). The freeze always expires
 * on its own via FreezeManager's countdown; "stop" lets an admin end it
 * early. The target can still look around, chat, and use their own
 * inventory - only walking/flying movement is blocked.
 */
public class KeyboardDisconnectCommand implements SubCommand {

    private static final int DEFAULT_SECONDS = 10;
    private static final int MAX_SECONDS = 120;

    private final FreezeManager freezeManager;

    public KeyboardDisconnectCommand(FreezeManager freezeManager) {
        this.freezeManager = freezeManager;
    }

    @Override
    public String getName() {
        return "keyboarddisconnect";
    }

    @Override
    public String getDescription() {
        return "Temporarily freezes a player's movement, with a countdown, for a set duration.";
    }

    @Override
    public String getSyntax() {
        return "/chaos keyboarddisconnect <player> [seconds|stop]";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("chaos.freeze")) {
            player.sendMessage(Component.text("You do not have permission to use this.", NamedTextColor.RED));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: " + getSyntax(), NamedTextColor.RED));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(Component.text("Player '" + args[1] + "' is not online.", NamedTextColor.RED));
            return;
        }

        if (args.length >= 3 && args[2].equalsIgnoreCase("stop")) {
            freezeManager.unfreeze(target.getUniqueId());
            player.sendMessage(Component.text("Unfroze " + target.getName() + ".", NamedTextColor.GREEN));
            return;
        }

        int seconds = DEFAULT_SECONDS;
        if (args.length >= 3) {
            try {
                seconds = Integer.parseInt(args[2]);
            } catch (NumberFormatException exception) {
                player.sendMessage(Component.text("'" + args[2] + "' is not a valid number of seconds.", NamedTextColor.RED));
                return;
            }
        }

        if (seconds <= 0 || seconds > MAX_SECONDS) {
            player.sendMessage(Component.text("Seconds must be between 1 and " + MAX_SECONDS + ".", NamedTextColor.RED));
            return;
        }

        freezeManager.freeze(target, seconds);

        target.sendMessage(Component.text("Your keyboard seems to have disconnected...", NamedTextColor.RED));
        player.sendMessage(Component.text(
            "Froze " + target.getName() + " for " + seconds + " seconds.", NamedTextColor.GREEN
        ));
    }
}
