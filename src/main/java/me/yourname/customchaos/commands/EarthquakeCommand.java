package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class EarthquakeCommand implements SubCommand {

    private static final double RADIUS = 10.0;

    @Override
    public String getName() {
        return "earthquake";
    }

    @Override
    public String getDescription() {
        return "Shakes the ground and nearby players around you.";
    }

    @Override
    public String getSyntax() {
        return "/chaos earthquake";
    }

    @Override
    public void perform(Player player, String[] args) {
        Location center = player.getLocation();

        center.getWorld().spawnParticle(Particle.CLOUD, center, 80, RADIUS / 2, 0.1, RADIUS / 2, 0.02);
        center.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 0.6f, 0.5f);

        for (Player nearby : center.getWorld().getPlayers()) {
            if (nearby.getLocation().distance(center) <= RADIUS) {
                Vector shake = new Vector(
                    (Math.random() - 0.5) * 0.6,
                    0.4,
                    (Math.random() - 0.5) * 0.6
                );
                nearby.setVelocity(shake);
                nearby.sendMessage(Component.text("The ground shakes beneath you!", NamedTextColor.GOLD));
            }
        }
    }
}
