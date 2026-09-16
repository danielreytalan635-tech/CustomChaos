package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FakeOpCommand implements SubCommand {

    @Override
    public String getName() {
        return "fakeop";
    }

    @Override
    public String getDescription() {
        return "Sends a target a fake 'you are now op' system-style message.";
    }

    @Override
    public String getSyntax() {
        return "/chaos fakeop <player>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(Component.text("Usage: " + getSyntax(), NamedTextColor.RED));
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null || !target.isOnline()) {
            player.sendMessage(Component.text("Player '" + targetName + "' is not online.", NamedTextColor.RED));
            return;
        }

        // Vanilla "You are now op!" style message uses gray, non-italic text.
        Component fakeMessage = Component.text("You are now op!", NamedTextColor.GRAY);
        target.sendMessage(fakeMessage);

        player.sendMessage(Component.text("Sent fake op message to " + target.getName() + ".", NamedTextColor.GREEN));
    }
}
