package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CreeperHissCommand implements SubCommand {

    @Override
    public String getName() {
        return "creeperhiss";
    }

    @Override
    public String getDescription() {
        return "Plays a localized creeper hiss sound at the target's location.";
    }

    @Override
    public String getSyntax() {
        return "/chaos creeperhiss <player>";
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

        Location targetLoc = target.getLocation();

        // Play only to the target's client, localized at their own coordinates.
        target.playSound(targetLoc, Sound.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);

        player.sendMessage(Component.text("Played creeper hiss for " + target.getName() + ".", NamedTextColor.GREEN));
    }
}
