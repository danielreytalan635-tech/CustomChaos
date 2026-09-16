package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PingCommand implements SubCommand {

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Shows your own or a target player's ping in milliseconds.";
    }

    @Override
    public String getSyntax() {
        return "/chaos ping [player]";
    }

    @Override
    public void perform(Player player, String[] args) {
        Player target = player;

        if (args.length >= 2) {
            Player found = Bukkit.getPlayerExact(args[1]);
            if (found == null) {
                player.sendMessage(Component.text("Player '" + args[1] + "' is not online.", NamedTextColor.RED));
                return;
            }
            target = found;
        }

        player.sendMessage(
            Component.text(target.getName() + "'s ping: ", NamedTextColor.GRAY)
                .append(Component.text(target.getPing() + "ms", NamedTextColor.GREEN))
        );
    }
}
