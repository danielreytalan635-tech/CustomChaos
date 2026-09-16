package me.yourname.customchaos;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Central router for "/chaos <subcommand> [args...]".
 * Holds every registered SubCommand in a HashMap keyed by name and dispatches
 * to the matching implementation. Also provides tab-completion for
 * subcommand names and online player names.
 */
public class CommandRouter implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommands = new LinkedHashMap<>();

    public void register(SubCommand subCommand) {
        subCommands.put(subCommand.getName().toLowerCase(), subCommand);
    }

    public Map<String, SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());

        if (subCommand == null) {
            player.sendMessage(Component.text("Unknown subcommand '" + args[0] + "'. Run /chaos for a list.", NamedTextColor.RED));
            return true;
        }

        try {
            subCommand.perform(player, args);
        } catch (Exception exception) {
            player.sendMessage(Component.text("Something went wrong running that command.", NamedTextColor.RED));
            player.getServer().getLogger().warning(
                "[CustomChaos] Error executing '" + subCommand.getName() + "': " + exception.getMessage()
            );
        }

        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(Component.text("=== CustomChaos Commands ===", NamedTextColor.GOLD));
        for (SubCommand sub : subCommands.values()) {
            player.sendMessage(
                Component.text(sub.getSyntax(), NamedTextColor.YELLOW)
                    .append(Component.text(" - " + sub.getDescription(), NamedTextColor.GRAY))
            );
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> results = new ArrayList<>();

        if (args.length == 1) {
            String input = args[0].toLowerCase();
            results.addAll(
                subCommands.keySet().stream()
                    .filter(name -> name.startsWith(input))
                    .collect(Collectors.toList())
            );
            return results;
        }

        if (args.length >= 2) {
            String input = args[args.length - 1].toLowerCase();
            for (Player online : sender.getServer().getOnlinePlayers()) {
                if (online.getName().toLowerCase().startsWith(input)) {
                    results.add(online.getName());
                }
            }
            return results;
        }

        return results;
    }
}
