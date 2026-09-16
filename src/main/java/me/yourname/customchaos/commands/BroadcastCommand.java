package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BroadcastCommand implements SubCommand {

    @Override
    public String getName() {
        return "broadcast";
    }

    @Override
    public String getDescription() {
        return "Broadcasts a message to the entire server.";
    }

    @Override
    public String getSyntax() {
        return "/chaos broadcast <message...>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("chaos.broadcast")) {
            player.sendMessage(Component.text("You do not have permission to broadcast.", NamedTextColor.RED));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: " + getSyntax(), NamedTextColor.RED));
            return;
        }

        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        Component broadcastMessage = Component.text("[Broadcast] ", NamedTextColor.GOLD)
            .append(Component.text(message, NamedTextColor.WHITE));

        Bukkit.broadcast(broadcastMessage);
    }
}
