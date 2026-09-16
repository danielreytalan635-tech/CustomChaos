package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class HealanimationCommand implements SubCommand {

    @Override
    public String getName() {
        return "healanimation";
    }

    @Override
    public String getDescription() {
        return "Heals a target player to full health with a visual effect.";
    }

    @Override
    public String getSyntax() {
        return "/chaos healanimation [player]";
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

        // Fixed: Updated old attribute reference to match modern Paper/Mojang specifications
        if (target.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
            double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            target.setHealth(maxHealth);
        } else {
            target.setHealth(20.0);
        }
        
        target.setFoodLevel(20);

        target.getWorld().spawnParticle(Particle.HEART, target.getLocation().add(0, 1.5, 0), 10, 0.4, 0.4, 0.4, 0.01);
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 1.5f);

        target.sendMessage(Component.text("You have been fully healed!", NamedTextColor.GREEN));
        if (!target.equals(player)) {
            player.sendMessage(Component.text("Healed " + target.getName() + ".", NamedTextColor.GREEN));
        }
    }
}
