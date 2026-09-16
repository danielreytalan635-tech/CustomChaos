package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

public class ConfettiCommand implements SubCommand {

    private static final Random RANDOM = new Random();
    private static final Color[] COLORS = {
        Color.RED, Color.ORANGE, Color.YELLOW, Color.LIME, Color.AQUA, Color.FUCHSIA
    };

    @Override
    public String getName() {
        return "confetti";
    }

    @Override
    public String getDescription() {
        return "Bursts colorful confetti particles around you.";
    }

    @Override
    public String getSyntax() {
        return "/chaos confetti";
    }

    @Override
    public void perform(Player player, String[] args) {
        for (int i = 0; i < 30; i++) {
            Color color = COLORS[RANDOM.nextInt(COLORS.length)];
            Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1.2f);
            player.getWorld().spawnParticle(
                Particle.DUST,
                player.getLocation().add(0, 1.2, 0),
                1,
                0.6, 0.6, 0.6,
                0,
                dustOptions
            );
        }

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0f, 1.0f);
        player.sendMessage(Component.text("Confetti!", NamedTextColor.LIGHT_PURPLE));
    }
}
