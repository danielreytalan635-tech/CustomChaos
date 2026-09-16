package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TimestopCommand implements SubCommand {

    private static final int DURATION_TICKS = 100; // 5 seconds

    @Override
    public String getName() {
        return "timestop";
    }

    @Override
    public String getDescription() {
        return "Freezes a target player in place for a few seconds.";
    }

    @Override
    public String getSyntax() {
        return "/chaos timestop <player>";
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

        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, DURATION_TICKS, 250, false, false));
        target.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, DURATION_TICKS, 5, false, false));

        target.getWorld().spawnParticle(Particle.PORTAL, target.getLocation().add(0, 1, 0), 40, 0.3, 0.6, 0.3, 0.1);
        target.getWorld().playSound(target.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.5f);

        target.sendMessage(Component.text("Time has stopped around you!", NamedTextColor.AQUA));
        player.sendMessage(Component.text("Froze " + target.getName() + " in place.", NamedTextColor.GREEN));
    }
}
