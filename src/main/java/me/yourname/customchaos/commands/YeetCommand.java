package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class YeetCommand implements SubCommand {

    @Override
    public String getName() {
        return "yeet";
    }

    @Override
    public String getDescription() {
        return "Launches a player straight up into the air.";
    }

    @Override
    public String getSyntax() {
        return "/chaos yeet <player>";
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

        target.setVelocity(new Vector(0, 1.8, 0));

        player.sendMessage(Component.text("You yeeted " + target.getName() + " into the sky!", NamedTextColor.GREEN));
        target.sendMessage(Component.text("You have been yeeted!", NamedTextColor.YELLOW));
    }
}
