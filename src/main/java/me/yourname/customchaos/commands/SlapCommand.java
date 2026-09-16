package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SlapCommand implements SubCommand {

    @Override
    public String getName() {
        return "slap";
    }

    @Override
    public String getDescription() {
        return "Slaps a player, knocking them backward away from your view direction.";
    }

    @Override
    public String getSyntax() {
        return "/chaos slap <player>";
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

        Location senderLoc = player.getLocation();
        Vector pushDirection = senderLoc.getDirection().normalize();

        // Push away from sender's look vector, with a strong upward component for a "slap" arc.
        double horizontalStrength = 1.5;
        double verticalStrength = 0.6;

        Vector knockback = new Vector(
                pushDirection.getX() * horizontalStrength,
                verticalStrength,
                pushDirection.getZ() * horizontalStrength
        );

        target.setVelocity(knockback);

        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);

        player.sendMessage(Component.text("You slapped " + target.getName() + "!", NamedTextColor.GREEN));
        target.sendMessage(Component.text(player.getName() + " slapped you!", NamedTextColor.RED));
    }
}
