package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Admin-only utility that dispatches an arbitrary command from the console,
 * optionally targeting a named player via a {player} placeholder.
 *
 * This intentionally does NOT grant the target OP, does NOT store any
 * persistent bypass state, and does NOT run anything on the target's
 * behalf without the admin explicitly typing the full command out.
 * Every use is a one-shot, permission-gated, fully attributable action.
 *
 * Example: /chaos sudo Steve gamemode survival {player}
 */
public class SudoCommand implements SubCommand {

    @Override
    public String getName() {
        return "sudo";
    }

    @Override
    public String getDescription() {
        return "Runs a command as console, optionally targeting a player. Requires chaos.sudo.";
    }

    @Override
    public String getSyntax() {
        return "/chaos sudo <player> <command...> (use {player} as a placeholder)";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("chaos.sudo")) {
            player.sendMessage(Component.text("You do not have permission to use sudo.", NamedTextColor.RED));
            return;
        }

        if (args.length < 3) {
            player.sendMessage(Component.text("Usage: " + getSyntax(), NamedTextColor.RED));
            return;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(Component.text("Player '" + args[1] + "' is not online.", NamedTextColor.RED));
            return;
        }

        String rawCommand = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        String resolvedCommand = rawCommand.replace("{player}", target.getName());

        player.sendMessage(Component.text("Dispatching as console: /" + resolvedCommand, NamedTextColor.GRAY));

        boolean success = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), resolvedCommand);

        if (!success) {
            player.sendMessage(Component.text("Command failed or was not recognized by the server.", NamedTextColor.RED));
        }
    }
}
