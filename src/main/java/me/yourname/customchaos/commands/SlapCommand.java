package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
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
        return "Slaps a target player with a burst of knockback.";
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

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            player.sendMessage(Component.text("Player '" + args[1] + "' is not online.", NamedTextColor.RED));
            return;
        }

        Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
        direction.setY(0.5);
        target.setVelocity(direction.multiply(1.5));

        target.getWorld().spawnParticle(Particle.SWEEP_ATTACK, target.getLocation().add(0, 1, 0), 3);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1.0f, 1.0f);

        target.sendMessage(Component.text(player.getName() + " slapped you!", NamedTextColor.YELLOW));
        player.sendMessage(Component.text("You slapped " + target.getName() + ".", NamedTextColor.GREEN));
    }
}
