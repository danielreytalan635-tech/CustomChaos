package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class YeetCommand implements SubCommand {

    @Override
    public String getName() {
        return "yeet";
    }

    @Override
    public String getDescription() {
        return "Launches yourself or a target player into the sky.";
    }

    @Override
    public String getSyntax() {
        return "/chaos yeet [player]";
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

        target.setVelocity(new Vector(0, 2.2, 0));
        target.getWorld().spawnParticle(Particle.CLOUD, target.getLocation(), 40, 0.5, 0.2, 0.5, 0.05);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.2f);
        target.sendMessage(Component.text("You have been yeeted!", NamedTextColor.YELLOW));

        if (!target.equals(player)) {
            player.sendMessage(Component.text("Yeeted " + target.getName() + ".", NamedTextColor.GREEN));
        }
    }
}
