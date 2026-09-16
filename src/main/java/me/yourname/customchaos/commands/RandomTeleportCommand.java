package me.yourname.customchaos.commands;

import me.yourname.customchaos.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class RandomTeleportCommand implements SubCommand {

    private static final Random RANDOM = new Random();
    private static final int DEFAULT_RADIUS = 500;
    private static final int MAX_ATTEMPTS = 10;

    @Override
    public String getName() {
        return "randomteleport";
    }

    @Override
    public String getDescription() {
        return "Teleports you to a random location within a radius of your position.";
    }

    @Override
    public String getSyntax() {
        return "/chaos randomteleport [radius]";
    }

    @Override
    public void perform(Player player, String[] args) {
        int radius = DEFAULT_RADIUS;

        if (args.length >= 2) {
            try {
                radius = Integer.parseInt(args[1]);
                if (radius < 10) {
                    player.sendMessage(Component.text("Radius must be at least 10.", NamedTextColor.RED));
                    return;
                }
            } catch (NumberFormatException exception) {
                player.sendMessage(Component.text("'" + args[1] + "' is not a valid number.", NamedTextColor.RED));
                return;
            }
        }

        World world = player.getWorld();
        Location origin = player.getLocation();

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int offsetX = RANDOM.nextInt(radius * 2) - radius;
            int offsetZ = RANDOM.nextInt(radius * 2) - radius;
            int x = origin.getBlockX() + offsetX;
            int z = origin.getBlockZ() + offsetZ;
            int y = world.getHighestBlockYAt(x, z);

            if (y <= world.getMinHeight() || y >= world.getMaxHeight() - 2) {
                continue;
            }

            Location destination = new Location(world, x + 0.5, y + 1, z + 0.5);
            player.teleport(destination);
            player.sendMessage(Component.text("Teleported to a random location!", NamedTextColor.AQUA));
            return;
        }

        player.sendMessage(Component.text("Couldn't find a safe location, try again.", NamedTextColor.RED));
    }
}
